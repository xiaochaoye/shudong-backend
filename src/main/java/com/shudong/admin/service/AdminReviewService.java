package com.shudong.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.message.entity.PrivateReplies;

public interface AdminReviewService {

    Page<PrivateReplies> getPendingReviews(int page, int size);

    void approveReview(Long reviewId, Long adminId, String ipAddress);

    void rejectReview(Long reviewId, String reason, Long adminId, String ipAddress);
}
