package com.shudong.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.message.entity.Notifications;
import com.shudong.message.mapper.NotificationsMapper;
import com.shudong.message.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author test
 * @description 站内通知服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationsMapper, Notifications>
    implements NotificationService {

    @Override
    public Notifications sendSystemNotification(Long userId, String title, String content, Long relatedId) {
        Notifications notification = new Notifications();
        notification.setUserId(userId);
        notification.setNoticeType("SYSTEM");
        notification.setTitle(title);
        notification.setNoticeBody(content);
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        this.save(notification);
        log.info("发送系统通知给用户 {}: {}", userId, title);
        return notification;
    }

    @Override
    public Notifications sendReplyNotification(Long userId, Long senderId, String title, String content, Long relatedId) {
        Notifications notification = new Notifications();
        notification.setUserId(userId);
        notification.setNoticeType("PRIVATE_REPLY");
        notification.setTitle(title);
        notification.setNoticeBody(content);
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        this.save(notification);
        log.info("发送回复通知给用户 {}: {}", userId, title);
        return notification;
    }

    @Override
    public List<Notifications> getUserNotifications(Long userId) {
        QueryWrapper<Notifications> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("created_at");
        return this.list(queryWrapper);
    }

    @Override
    public long getUnreadCount(Long userId) {
        QueryWrapper<Notifications> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("is_read", 0);
        return this.count(queryWrapper);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notifications notification = this.getById(notificationId);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setIsRead(1);
            this.updateById(notification);
            log.info("标记通知 {} 为已读", notificationId);
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        QueryWrapper<Notifications> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("is_read", 0);
        List<Notifications> unreadNotifications = this.list(queryWrapper);
        for (Notifications notification : unreadNotifications) {
            notification.setIsRead(1);
        }
        this.updateBatchById(unreadNotifications);
        log.info("标记用户 {} 所有通知为已读", userId);
    }
}
