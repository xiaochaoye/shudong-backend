package com.shudong.message.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.common.config.SseEmitterManager;
import com.shudong.common.response.Result;
import com.shudong.message.entity.Notifications;
import com.shudong.message.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SseEmitterManager sseEmitterManager;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestAttribute("userId") Long userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        emitter.onCompletion(() -> sseEmitterManager.removeEmitter(userId));
        emitter.onTimeout(() -> sseEmitterManager.removeEmitter(userId));
        emitter.onError(e -> sseEmitterManager.removeEmitter(userId));

        sseEmitterManager.addEmitter(userId, emitter);

        try {
            emitter.send(SseEmitter.event().name("connected").data("SSE连接成功"));
        } catch (Exception e) {
            log.warn("发送SSE初始事件失败: {}", e.getMessage());
            emitter.complete();
        }

        return emitter;
    }

    @GetMapping
    public Result<Page<Notifications>> getNotifications(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            Page<Notifications> notifications = notificationService.getUserNotifications(userId, page, size);
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
