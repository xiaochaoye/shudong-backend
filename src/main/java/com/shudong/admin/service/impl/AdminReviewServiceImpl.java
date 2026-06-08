package com.shudong.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.service.AdminLogService;
import com.shudong.admin.service.AdminReviewService;
import com.shudong.common.exception.BusinessException;
import com.shudong.message.entity.PrivateReplies;
import com.shudong.message.mapper.PrivateRepliesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService {

    private final PrivateRepliesMapper privateRepliesMapper;
    private final AdminLogService adminLogService;

    @Override
    public Page<PrivateReplies> getPendingReviews(int page, int size) {
        LambdaQueryWrapper<PrivateReplies> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrivateReplies::getReplyStatus, "PENDING")
                .orderByDesc(PrivateReplies::getCreatedAt);
        return privateRepliesMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    @Transactional
    public void approveReview(Long reviewId, Long adminId, String ipAddress) {
        PrivateReplies reply = privateRepliesMapper.selectById(reviewId);
        if (reply == null) {
            throw new BusinessException("审核记录不存在");
        }
        reply.setReplyStatus("APPROVED");
        reply.setReviewedAt(new Date());
        privateRepliesMapper.updateById(reply);
        adminLogService.logAction(adminId, "APPROVE_REVIEW", "PRIVATE_REPLY", reviewId, "审核通过", ipAddress);
    }

    @Override
    @Transactional
    public void rejectReview(Long reviewId, String reason, Long adminId, String ipAddress) {
        PrivateReplies reply = privateRepliesMapper.selectById(reviewId);
        if (reply == null) {
            throw new BusinessException("审核记录不存在");
        }
        reply.setReplyStatus("REJECTED");
        reply.setReviewedAt(new Date());
        privateRepliesMapper.updateById(reply);
        adminLogService.logAction(adminId, "REJECT_REVIEW", "PRIVATE_REPLY", reviewId,
                "审核拒绝, 原因: " + reason, ipAddress);
    }
}
