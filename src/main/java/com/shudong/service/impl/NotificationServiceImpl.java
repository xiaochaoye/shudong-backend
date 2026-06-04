package com.shudong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.mapper.NotificationMapper;
import com.shudong.model.entity.Notification;
import com.shudong.service.NotificationService;
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
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
    implements NotificationService {

    @Override
    public Notification sendSystemNotification(Long userId, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(null);
        notification.setType("system");
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        this.save(notification);
        log.info("发送系统通知给用户 {}: {}", userId, title);
        return notification;
    }

    @Override
    public Notification sendReplyNotification(Long userId, Long senderId, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setType("reply");
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        this.save(notification);
        log.info("发送回复通知给用户 {}: {}", userId, title);
        return notification;
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("created_at");
        return this.list(queryWrapper);
    }

    @Override
    public long getUnreadCount(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("is_read", 0);
        return this.count(queryWrapper);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = this.getById(notificationId);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setIsRead(1);
            notification.setReadAt(new Date());
            this.updateById(notification);
            log.info("标记通知 {} 为已读", notificationId);
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("is_read", 0);
        List<Notification> unreadNotifications = this.list(queryWrapper);
        Date now = new Date();
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(1);
            notification.setReadAt(now);
        }
        this.updateBatchById(unreadNotifications);
        log.info("标记用户 {} 所有通知为已读", userId);
    }
}
