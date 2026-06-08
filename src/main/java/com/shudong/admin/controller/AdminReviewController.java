package com.shudong.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.dto.ReviewRequest;
import com.shudong.admin.service.AdminReviewService;
import com.shudong.common.response.Result;
import com.shudong.message.entity.PrivateReplies;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;
    private final UsersMapper usersMapper;

    @GetMapping("/pending")
    public Result<Page<PrivateReplies>> getPendingReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(adminReviewService.getPendingReviews(page, size));
    }

    @PostMapping("/{reviewId}/approve")
    public Result<Void> approveReview(@PathVariable Long reviewId, HttpServletRequest request) {
        adminReviewService.approveReview(reviewId, getCurrentAdminId(), getClientIp(request));
        return Result.success();
    }

    @PostMapping("/{reviewId}/reject")
    public Result<Void> rejectReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest,
                                     HttpServletRequest request) {
        adminReviewService.rejectReview(reviewId, reviewRequest.getReason(), getCurrentAdminId(), getClientIp(request));
        return Result.success();
    }

    private Long getCurrentAdminId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        String email = auth.getName();
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("email", email));
        return user != null ? user.getId() : null;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
