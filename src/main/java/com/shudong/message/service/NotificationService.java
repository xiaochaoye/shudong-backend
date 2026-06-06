package com.shudong.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.message.entity.Notifications;

import java.util.List;

/**
 * @author test
 * @description 站内通知服务
 */
public interface NotificationService extends IService<Notifications> {

    /**
     * 发送系统通知
     * @param userId 接收者用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param relatedId 关联ID
     * @return 创建的通知
     */
    Notifications sendSystemNotification(Long userId, String title, String content, Long relatedId);

    /**
     * 发送回复通知
     * @param userId 接收者用户ID
     * @param senderId 发送者用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param relatedId 关联ID
     * @return 创建的通知
     */
    Notifications sendReplyNotification(Long userId, Long senderId, String title, String content, Long relatedId);

    /**
     * 获取用户的通知列表
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notifications> getUserNotifications(Long userId);

    /**
     * 获取用户未读通知数量
     * @param userId 用户ID
     * @return 未读数量
     */
    long getUnreadCount(Long userId);

    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     * @param userId 用户ID
     */
    void markAsRead(Long notificationId, Long userId);

    /**
     * 标记用户所有通知为已读
     * @param userId 用户ID
     */
    void markAllAsRead(Long userId);
}
