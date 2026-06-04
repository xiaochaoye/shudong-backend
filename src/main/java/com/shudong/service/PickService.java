package com.shudong.service;

import com.shudong.model.entity.Posts;

import java.util.List;

/**
 * @author test
 * @description 拾取服务
 */
public interface PickService {

    /**
     * 每日精选拾取
     * @param userId 用户ID
     * @param limit 拾取数量
     * @return 帖子列表
     */
    List<Posts> dailyPick(Long userId, int limit);

    /**
     * 夜间精选拾取
     * @param userId 用户ID
     * @param limit 拾取数量
     * @return 帖子列表
     */
    List<Posts> nightPick(Long userId, int limit);

    /**
     * 随机拾取
     * @param userId 用户ID
     * @param limit 拾取数量
     * @return 帖子列表
     */
    List<Posts> randomPick(Long userId, int limit);

    /**
     * 标记拾取记录为已回应
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void markAsReplied(Long userId, Long postId);

    /**
     * 检查用户是否超过拾取频率限制
     * @param userId 用户ID
     * @param pickType 拾取类型
     * @return 是否超过限制
     */
    boolean isRateLimited(Long userId, String pickType);

    /**
     * 获取用户今日拾取次数
     * @param userId 用户ID
     * @param pickType 拾取类型
     * @return 拾取次数
     */
    int getTodayPickCount(Long userId, String pickType);
}
