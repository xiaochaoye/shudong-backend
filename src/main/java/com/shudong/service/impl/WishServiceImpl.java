package com.shudong.service.impl;

import com.shudong.exception.BusinessException;
import com.shudong.mapper.PostsMapper;
import com.shudong.mapper.UsersMapper;
import com.shudong.model.dto.*;
import com.shudong.model.entity.Posts;
import com.shudong.model.entity.Users;
import com.shudong.model.enums.WishStatus;
import com.shudong.model.vo.WishVO;
import com.shudong.service.WishService;
import com.shudong.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 许愿池服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WishServiceImpl extends ServiceImpl<PostsMapper, Posts> implements WishService {

    private final PostsMapper postsMapper;

    private final UsersMapper usersMapper;

    private final RedisUtil redisUtil;

    private static final int DAILY_RANDOM_LIMIT = 10;
    private static final String WISH_RANDOM_COUNT_KEY_PREFIX = "wish:random:count:";

    @Override
    public List<WishVO> getMyWishes(Long userId) {
        try {
            QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("post_type", "wish").isNull("deleted_at").orderByDesc("created_at");

            List<Posts> posts = postsMapper.selectList(queryWrapper);
            List<WishVO> wishVOs = posts.stream().map(this::convertToWishVO).collect(Collectors.toList());

            return wishVOs;
        } catch (Exception e) {
            log.error("获取我的愿望失败，用户ID: {}", userId, e);
            throw new RuntimeException("获取愿望列表失败");
        }
    }

    @Override
    @Transactional
    public WishVO updateWish(Long wishId, WishUpdateDTO updateDTO, Long userId) {
        try {
            Posts wish = postsMapper.selectById(wishId);
            if (wish == null) {
                throw new RuntimeException("愿望不存在");
            }

            if (!wish.getUserId().equals(userId)) {
                throw new RuntimeException("无权编辑此愿望");
            }

            if (!"wish".equals(wish.getPostType())) {
                throw new RuntimeException("非许愿池内容");
            }

            if (WishStatus.COMPLETED.name().equals(wish.getWishStatus())) {
                throw new RuntimeException("已实现的愿望不能编辑");
            }

            // 更新愿望信息
            wish.setTitle(updateDTO.getTitle());
            wish.setContent(updateDTO.getContent());
            wish.setIsAnonymous(updateDTO.getIsAnonymous() ? 1 : 0);
            wish.setUpdatedAt(new Date());

            int result = postsMapper.updateById(wish);
            if (result > 0) {
                WishVO wishVO = convertToWishVO(wish);
                return wishVO;
            } else {
                throw new RuntimeException("更新愿望失败");
            }
        } catch (Exception e) {
            log.error("更新愿望失败，愿望ID: {}, 用户ID: {}", wishId, userId, e);
            throw new RuntimeException("更新愿望失败");
        }
    }

    @Override
    @Transactional
    public Boolean deleteWish(Long wishId, Long userId) {
        try {
            Posts wish = postsMapper.selectById(wishId);
            if (wish == null) {
                throw new RuntimeException("愿望不存在");
            }

            if (!wish.getUserId().equals(userId)) {
                throw new RuntimeException("无权删除此愿望");
            }

            if (!"wish".equals(wish.getPostType())) {
                throw new RuntimeException("非许愿池内容");
            }

            // 软删除
            wish.setDeletedAt(new Date());
            int result = postsMapper.updateById(wish);

            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除愿望失败");
        }
    }

    /**
     * Redis 原子计数 +　使用主键范围扫描法获取随机愿望
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WishVO getRandomWish(Long userId) {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE); 
        String redisKey = WISH_RANDOM_COUNT_KEY_PREFIX + userId + ":" + today;

        Long currentCount = redisUtil.incr(redisKey, 1);
        redisUtil.expire(redisKey, 24 * 60 * 60); // 确保 TTL

        if (currentCount > DAILY_RANDOM_LIMIT) {
            redisUtil.decr(redisKey, 1); // 回退一次计数
            throw new BusinessException("今日随机取愿望次数已用完");
        }

        QueryWrapper<Posts> countWrapper = new QueryWrapper<>();
        countWrapper.eq("post_type", "wish").eq("wish_status", WishStatus.PENDING.name()).isNull("deleted_at");

        Map<String, Object> minMax = postsMapper
                .selectMaps(countWrapper.select("MIN(id) as min_id, MAX(id) as max_id").last("LIMIT 1")).get(0);

        Long minId = (Long) minMax.get("min_id");
        Long maxId = (Long) minMax.get("max_id");

        if (minId == null) {
            throw new BusinessException("暂无未实现的愿望");
        }

        // 尝试最多 5 次
        for (int i = 0; i < 5; i++) {
            // 在 ID 范围内随机选一个起点
            long randomId = ThreadLocalRandom.current().nextLong(minId, maxId + 1);

            QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("post_type", "wish").eq("wish_status", WishStatus.PENDING.name()).isNull("deleted_at")
                    .ge("id", randomId) // 从随机 ID 开始找
                    .last("ORDER BY id ASC LIMIT 1");

            List<Posts> candidates = postsMapper.selectList(queryWrapper);
            if (!candidates.isEmpty()) {
                Posts randomWish = candidates.get(0);

                Integer dbCount = Optional.ofNullable(randomWish.getDailyRandomCount()).orElse(0);
                randomWish.setDailyRandomCount(dbCount + 1);
                randomWish.setLastRandomDate(new java.sql.Date(System.currentTimeMillis()));
                postsMapper.updateById(randomWish);

                return convertToWishVO(randomWish);
            }
        }

        // 如果主键扫描失败，回退到 LIMIT offset 方案
        long total = postsMapper.selectCount(countWrapper);
        if (total == 0) {
            throw new BusinessException("暂无未实现的愿望");
        }
        int offset = ThreadLocalRandom.current().nextInt((int) total);
        Posts fallbackWish = postsMapper.selectOne(countWrapper.last("LIMIT " + offset + ", 1"));
        if (fallbackWish == null) {
            throw new BusinessException("回退方案仍未找到愿望");
        }

        Integer dbCount = Optional.ofNullable(fallbackWish.getDailyRandomCount()).orElse(0);
        fallbackWish.setDailyRandomCount(dbCount + 1);
        fallbackWish.setLastRandomDate(new java.sql.Date(System.currentTimeMillis()));
        postsMapper.updateById(fallbackWish);

        return convertToWishVO(fallbackWish);
    }

    @Override
    @Transactional
    public WishVO createWish(WishCreateDTO createDTO, Long userId) {
        try {
            Posts wish = new Posts();
            wish.setUserId(userId);
            wish.setTitle(createDTO.getTitle());
            wish.setContent(createDTO.getContent());
            wish.setPostType("wish");
            wish.setIsAnonymous(createDTO.getIsAnonymous() ? 1 : 0);
            wish.setWishStatus(WishStatus.PENDING.name());
            wish.setLikeCount(0);
            wish.setDailyRandomCount(0);
            wish.setCreatedAt(new Date());
            wish.setUpdatedAt(new Date());

            int result = postsMapper.insert(wish);
            if (result > 0) {
                WishVO wishVO = convertToWishVO(wish);
                return wishVO;
            } else {
                throw new RuntimeException("创建愿望失败");
            }
        } catch (Exception e) {
            log.error("创建愿望失败，用户ID: {}", userId, e);
            throw new RuntimeException("创建愿望失败");
        }
    }

    @Override
    @Transactional
    public WishVO completeWish(WishCompleteDTO completeDTO, Long userId) {
        try {
            Posts wish = postsMapper.selectById(completeDTO.getWishId());
            if (wish == null) {
                throw new RuntimeException("愿望不存在");
            }

            if (!"wish".equals(wish.getPostType())) {
                throw new RuntimeException("非许愿池内容");
            }

            if (WishStatus.COMPLETED.name().equals(wish.getWishStatus())) {
                throw new RuntimeException("愿望已完成");
            }

            // 标记愿望为已完成
            wish.setWishStatus(WishStatus.COMPLETED.name());
            wish.setCompletedAt(new Date());
            wish.setCompletedBy(userId);
            wish.setUpdatedAt(new Date());

            int result = postsMapper.updateById(wish);
            if (result > 0) {
                WishVO wishVO = convertToWishVO(wish);
                return wishVO;
            } else {
                throw new RuntimeException("完成愿望失败");
            }
        } catch (Exception e) {
            log.error("完成愿望失败，愿望ID: {}, 用户ID: {}", completeDTO.getWishId(), userId, e);
            throw new RuntimeException("完成愿望失败");
        }
    }

    @Override
    public Object getWishStats() {
        try {
            QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("post_type", "wish").isNull("deleted_at");

            List<Posts> allWishes = postsMapper.selectList(queryWrapper);

            long totalCount = allWishes.size();
            long completedCount = allWishes.stream()
                    .filter(wish -> WishStatus.COMPLETED.name().equals(wish.getWishStatus())).count();
            long pendingCount = totalCount - completedCount;

            WishStatsDTO stats = new WishStatsDTO();
            stats.setTotalCount(totalCount);
            stats.setCompletedCount(completedCount);
            stats.setPendingCount(pendingCount);

            return stats;
        } catch (Exception e) {
            log.error("获取愿望统计失败", e);
            throw new RuntimeException("获取愿望统计失败");
        }
    }

    /**
     * 将Posts实体转换为WishVO
     */
    private WishVO convertToWishVO(Posts post) {
        WishVO wishVO = new WishVO();
        BeanUtils.copyProperties(post, wishVO);
        wishVO.setId(post.getId());

        // 设置用户信息（匿名时隐藏）
        // isAnonymous: 0-不匿名, 1-匿名
        if (post.getIsAnonymous() != null && post.getIsAnonymous() == 0) {
            Users user = usersMapper.selectById(post.getUserId());
            if (user != null) {
                wishVO.setUsername(user.getUsername());
                wishVO.setAvatar(user.getAvatar());
            }
        }

        // 设置完成者信息
        if (post.getCompletedBy() != null) {
            Users completedByUser = usersMapper.selectById(post.getCompletedBy());
            if (completedByUser != null) {
                wishVO.setCompletedByUsername(completedByUser.getUsername());
            }
        }

        return wishVO;
    }

}
