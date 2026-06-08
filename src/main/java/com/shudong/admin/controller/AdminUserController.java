package com.shudong.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.dto.UserResponse;
import com.shudong.admin.service.AdminUserService;
import com.shudong.common.response.Result;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final UsersMapper usersMapper;

    @GetMapping
    public Result<Page<UserResponse>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return Result.success(adminUserService.getUserList(page, size, keyword, status));
    }

    @GetMapping("/{userId}")
    public Result<UserResponse> getUserDetail(@PathVariable Long userId) {
        return Result.success(adminUserService.getUserDetail(userId));
    }

    @PutMapping("/{userId}/status")
    public Result<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam String status,
            @RequestParam String reason,
            HttpServletRequest request) {
        adminUserService.updateUserStatus(userId, status, reason, getCurrentAdminId(), getClientIp(request));
        return Result.success();
    }

    @DeleteMapping("/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        adminUserService.deleteUser(userId, getCurrentAdminId(), getClientIp(request));
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
        // X-Forwarded-For 可能含多个IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
