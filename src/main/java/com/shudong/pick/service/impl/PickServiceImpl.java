package com.shudong.pick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shudong.common.utils.RedisUtil;
import com.shudong.pick.entity.PickRecords;
import com.shudong.pick.mapper.PickRecordsMapper;
import com.shudong.pick.service.PickService;
import com.shudong.post.entity.Posts;
import com.shudong.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author test
 * @description 拾取服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PickServiceImpl implements PickService {

    private final PickRecordsMapper pickRecordMapper;
    private final PostsMapper postsMapper;
    private final RedisUtil redisUtil;

    /**
     * 每日拾取上限
     */
    private static final int DAILY_PICK_LIMIT = 20;

    /**
     * 夜间拾取上限
     */
    private static final int NIGHT_PICK_LIMIT = 40;

    /**
     * 随机拾取上限
     */
    private static final int RANDOM_PICK_LIMIT = 10;

    @Override
    public List<Posts> dailyPick(Long userId, int limit) {
        if (isRateLimited(userId, "daily")) {
            return Collections.emptyList();
        }
        List<Posts> posts = pickPosts(userId, limit, "daily");
        recordPick(userId, posts, "daily");
        return posts;
    }

    @Override
    public List<Posts> nightPick(Long userId, int limit) {
        if (isRateLimited(userId, "night")) {
            return Collections.emptyList();
        }
        List<Posts> posts = pickPosts(userId, limit, "night");
        recordPick(userId, posts, "night");
        return posts;
    }

    @Override
    public List<Posts> randomPick(Long userId, int limit) {
        if (isRateLimited(userId, "random")) {
            return Collections.emptyList();
        }
        List<Posts> posts = pickPosts(userId, limit, "random");
        recordPick(userId, posts, "random");
        return posts;
    }

    @Override
    public void markAsReplied(Long userId, Long postId) {
        QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        PickRecords record = pickRecordMapper.selectOne(queryWrapper);
        if (record != null) {
            record.setResonancedAt(new Date());
            pickRecordMapper.updateById(record);
        }
    }

    @Override
    public boolean isRateLimited(Long userId, String pickType) {
        int limit;
        switch (pickType) {
            case "daily":
                limit = DAILY_PICK_LIMIT;
                break;
            case "night":
                limit = NIGHT_PICK_LIMIT;
                break;
            case "random":
                limit = RANDOM_PICK_LIMIT;
                break;
            default:
                limit = DAILY_PICK_LIMIT;
        }
        int count = getTodayPickCount(userId, pickType);
        return count >= limit;
    }

    @Override
    public int getTodayPickCount(Long userId, String pickType) {
        String key = "pick:" + pickType + ":" + userId + ":" + LocalDate.now();
        Object count = redisUtil.get(key);
        if (count == null) {
            // 从数据库查询今日拾取次数
            Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .eq("pick_type", pickType)
                    .ge("picked_at", today);
            Long dbCount = pickRecordMapper.selectCount(queryWrapper);
            int result = dbCount != null ? dbCount.intValue() : 0;
            // 缓存到当天结束
            redisUtil.set(key, result, getSecondsUntilEndOfDay());
            return result;
        }
        return Integer.parseInt(count.toString());
    }

    /**
     * 组合策略算法：未被回应优先 + 时间衰减 + 去重
     */
    private List<Posts> pickPosts(Long userId, int limit, String pickType) {
        // 1. 获取用户已拾取的帖子ID列表（去重）
        Set<Long> pickedPostIds = getPickedPostIds(userId);

        // 2. 获取所有可拾取的帖子（排除已删除和已拾取的）
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at")
                .eq("is_published", 1)
                .notIn("id", pickedPostIds.isEmpty() ? Collections.singletonList(-1L) : pickedPostIds)
                .orderByDesc("created_at");
        List<Posts> allPosts = postsMapper.selectList(queryWrapper);

        if (allPosts.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 组合策略排序
        List<PostScore> scoredPosts = allPosts.stream()
                .map(post -> new PostScore(post, calculateScore(post, userId)))
                .sorted(Comparator.comparingDouble(PostScore::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return scoredPosts.stream()
                .map(PostScore::getPost)
                .collect(Collectors.toList());
    }

    /**
     * 计算帖子得分：未被回应优先 + 时间衰减
     */
    private double calculateScore(Posts post, Long userId) {
        double score = 0.0;

        // 1. 未被回应优先：检查该帖子是否被当前用户拾取过且未回应（resonanced_at 为 null 表示未回应）
        QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("post_id", post.getId())
                .isNull("resonanced_at");
        Long unrepliedCount = pickRecordMapper.selectCount(queryWrapper);
        if (unrepliedCount != null && unrepliedCount > 0) {
            score += 100.0; // 高优先级
        }

        // 2. 时间衰减：越新的帖子得分越高
        Date createdAt = post.getCreatedAt();
        if (createdAt != null) {
            long daysSinceCreated = (System.currentTimeMillis() - createdAt.getTime()) / (1000 * 60 * 60 * 24);
            double timeDecay = Math.exp(-daysSinceCreated / 7.0); // 7天衰减周期
            score += timeDecay * 50.0;
        }

        // 3. 浏览数权重：浏览数适中的帖子更可能被需要回应
        Object viewCountObj = post.getViewCount();
        if (viewCountObj != null) {
            try {
                int viewCount = Integer.parseInt(viewCountObj.toString());
                score += Math.min(viewCount / 10.0, 20.0); // 最多加20分
            } catch (NumberFormatException e) {
                // 忽略解析错误
            }
        }

        return score;
    }

    /**
     * 获取用户已拾取的帖子ID
     */
    private Set<Long> getPickedPostIds(Long userId) {
        QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<PickRecords> records = pickRecordMapper.selectList(queryWrapper);
        return records.stream()
                .map(PickRecords::getPostId)
                .collect(Collectors.toSet());
    }

    /**
     * 记录拾取
     */
    private void recordPick(Long userId, List<Posts> posts, String pickType) {
        if (posts == null || posts.isEmpty()) {
            return;
        }

        Date now = new Date();
        for (Posts post : posts) {
            PickRecords record = new PickRecords();
            record.setUserId(userId);
            record.setPostId(post.getId());
            record.setPickType(pickType);
            record.setPickedAt(now);
            pickRecordMapper.insert(record);
        }

        // 更新Redis计数
        String key = "pick:" + pickType + ":" + userId + ":" + LocalDate.now();
        Object count = redisUtil.get(key);
        int newCount = (count == null) ? posts.size() : Integer.parseInt(count.toString()) + posts.size();
        redisUtil.set(key, newCount, getSecondsUntilEndOfDay());
    }

    /**
     * 获取距离当天结束的秒数
     */
    private long getSecondsUntilEndOfDay() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return (calendar.getTimeInMillis() - now.getTime()) / 1000;
    }

    /**
     * 帖子分数包装类
     */
    private static class PostScore {
        private final Posts post;
        private final double score;

        PostScore(Posts post, double score) {
            this.post = post;
            this.score = score;
        }

        Posts getPost() {
            return post;
        }

        double getScore() {
            return score;
        }
    }
}
