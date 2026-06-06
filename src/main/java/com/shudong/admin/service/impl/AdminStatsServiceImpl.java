package com.shudong.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shudong.admin.service.AdminStatsService;
import com.shudong.post.entity.Posts;
import com.shudong.post.mapper.CollectionMapper;
import com.shudong.post.mapper.CommentsMapper;
import com.shudong.post.mapper.PostsMapper;
import com.shudong.post.mapper.ResonancesMapper;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author test
 * @description 管理员统计服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {

    private final UsersMapper usersMapper;
    private final PostsMapper postsMapper;
    private final CommentsMapper commentsMapper;
    private final ResonancesMapper resonanceMapper;
    private final CollectionMapper collectionMapper;

    @Override
    public Map<String, Object> getUserStats() {
        long totalUsers = usersMapper.selectCount(null);
        long todayUsers = getTodayUsersCount();
        long activeUsers = getActiveUsersCount();

        return Map.of(
                "totalUsers", totalUsers,
                "todayUsers", todayUsers,
                "activeUsers", activeUsers
        );
    }

    @Override
    public Map<String, Object> getPostStats() {
        long totalPosts = postsMapper.selectCount(null);
        long todayPosts = getTodayPostsCount();
        long totalComments = commentsMapper.selectCount(null);

        return Map.of(
                "totalPosts", totalPosts,
                "todayPosts", todayPosts,
                "totalComments", totalComments
        );
    }

    @Override
    public Map<String, Object> getInteractionStats() {
        long totalResonances = resonanceMapper.selectCount(null);
        long totalCollections = collectionMapper.selectCount(null);
        long totalComments = commentsMapper.selectCount(null);

        return Map.of(
                "totalResonances", totalResonances,
                "totalCollections", totalCollections,
                "totalComments", totalComments
        );
    }

    @Override
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.putAll(getUserStats());
        overview.putAll(getPostStats());
        overview.putAll(getInteractionStats());
        overview.put("timestamp", new Date());
        return overview;
    }

    private long getTodayUsersCount() {
        Date today = getStartOfDay();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("created_at", today);
        return usersMapper.selectCount(queryWrapper);
    }

    private long getActiveUsersCount() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date weekAgo = calendar.getTime();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("created_at", weekAgo);
        return usersMapper.selectCount(queryWrapper);
    }

    private long getTodayPostsCount() {
        Date today = getStartOfDay();
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("created_at", today);
        return postsMapper.selectCount(queryWrapper);
    }

    private Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
