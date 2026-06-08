package com.shudong.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.dto.UserResponse;

public interface AdminUserService {

    Page<UserResponse> getUserList(int page, int size, String keyword, String status);

    UserResponse getUserDetail(Long userId);

    void updateUserStatus(Long userId, String status, String reason, Long adminId, String ipAddress);

    void deleteUser(Long userId, Long adminId, String ipAddress);
}
