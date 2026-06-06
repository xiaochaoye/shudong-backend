package com.shudong.admin.service;

import java.util.Map;

/**
 * @author test
 * @description 管理员统计服务
 */
public interface AdminStatsService {

    /**
     * 获取用户统计
     * @return 用户统计信息
     */
    Map<String, Object> getUserStats();

    /**
     * 获取帖子统计
     * @return 帖子统计信息
     */
    Map<String, Object> getPostStats();

    /**
     * 获取互动统计
     * @return 互动统计信息
     */
    Map<String, Object> getInteractionStats();

    /**
     * 获取系统概览
     * @return 系统概览信息
     */
    Map<String, Object> getSystemOverview();
}
