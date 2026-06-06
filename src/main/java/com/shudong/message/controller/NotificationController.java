package com.shudong.message.controller;

import com.shudong.common.response.Result;
import com.shudong.message.entity.Notifications;
import com.shudong.message.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取当前用户的通知列表
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @return 通知列表
     */
    @GetMapping
    public Result<List<Notifications>> getNotifications(@RequestAttribute("userId") Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            List<Notifications> notifications = notificationService.getUserNotifications(userId);
            return Result.success("获取通知列表成功", notifications);
        } catch (Exception e) {
            log.error("获取通知列表失败: {}", e.getMessage());
            return Result.error("获取通知列表失败");
        }
    }

    /**
     * 获取未读通知数量
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @return 未读数量
     */
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount(@RequestAttribute("userId") Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            long count = notificationService.getUnreadCount(userId);
            return Result.success("获取未读数量成功", count);
        } catch (Exception e) {
            log.error("获取未读数量失败: {}", e.getMessage());
            return Result.error("获取未读数量失败");
        }
    }

    /**
     * 标记通知为已读
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param notificationId 通知ID
     * @return 操作结果
     */
    @PutMapping("/{notificationId}/read")
    public Result<Void> markAsRead(@RequestAttribute("userId") Long userId,
                                   @PathVariable Long notificationId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            notificationService.markAsRead(notificationId, userId);
            return Result.success("标记已读成功");
        } catch (Exception e) {
            log.error("标记已读失败: {}", e.getMessage());
            return Result.error("标记已读失败");
        }
    }

    /**
     * 标记所有通知为已读
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @return 操作结果
     */
    @PutMapping("/read-all")
    public Result<Void> markAllAsRead(@RequestAttribute("userId") Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            notificationService.markAllAsRead(userId);
            return Result.success("全部标记已读成功");
        } catch (Exception e) {
            log.error("全部标记已读失败: {}", e.getMessage());
            return Result.error("全部标记已读失败");
        }
    }
}
