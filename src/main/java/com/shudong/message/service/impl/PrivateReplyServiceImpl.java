package com.shudong.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.common.exception.BusinessException;
import com.shudong.message.entity.PrivateReplies;
import com.shudong.message.mapper.PrivateRepliesMapper;
import com.shudong.message.service.MailService;
import com.shudong.message.service.NotificationService;
import com.shudong.message.service.PrivateReplyService;
import com.shudong.post.entity.Posts;
import com.shudong.post.mapper.PostsMapper;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateReplyServiceImpl extends ServiceImpl<PrivateRepliesMapper, PrivateReplies>
        implements PrivateReplyService {

    private final PostsMapper postsMapper;
    private final UsersMapper usersMapper;
    private final NotificationService notificationService;
    private final MailService mailService;

    @Override
    @Transactional
    public void sendPrivateReply(Long senderId, Long postId, String replyBody) {
        Posts post = postsMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        if ("DELETED".equals(post.getPostStatus())) {
            throw new BusinessException("帖子已删除，无法回复");
        }

        Users receiver = usersMapper.selectById(post.getUserId());
        if (receiver == null || "DELETED".equals(receiver.getRecordStatus())) {
            throw new BusinessException("该用户已注销，无法回复");
        }

        PrivateReplies reply = new PrivateReplies();
        reply.setPostId(postId);
        reply.setSenderId(senderId);
        reply.setReceiverId(post.getUserId());
        reply.setReplyBody(replyBody);
        reply.setReplyStatus("PENDING");
        reply.setCreatedAt(new Date());

        this.save(reply);
        log.info("用户 {} 向帖子 {} 发送私信回复，进入审核队列", senderId, postId);
    }

    @Override
    @Transactional
    public void approveReply(Long replyId) {
        PrivateReplies reply = this.getById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }

        reply.setReplyStatus("APPROVED");
        reply.setReviewedAt(new Date());
        this.updateById(reply);

        notificationService.sendReplyNotification(
                reply.getReceiverId(),
                reply.getSenderId(),
                "收到一条私信回复",
                "有人给你的树洞帖子回复了一条私信，请查收邮件。",
                reply.getPostId()
        );

        mailService.sendPrivateReplyEmail(replyId);
        log.info("私信回复 {} 审核通过，已发送邮件", replyId);
    }

    @Override
    @Transactional
    public void rejectReply(Long replyId) {
        PrivateReplies reply = this.getById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }

        reply.setReplyStatus("REJECTED");
        reply.setReviewedAt(new Date());
        this.updateById(reply);

        log.info("私信回复 {} 审核未通过", replyId);
    }
}
