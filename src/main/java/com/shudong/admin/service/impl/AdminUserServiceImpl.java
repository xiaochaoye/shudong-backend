package com.shudong.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.dto.UserResponse;
import com.shudong.admin.service.AdminLogService;
import com.shudong.admin.service.AdminUserService;
import com.shudong.common.exception.BusinessException;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UsersMapper usersMapper;
    private final AdminLogService adminLogService;

    @Override
    public Page<UserResponse> getUserList(int page, int size, String keyword, String status) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Users::getUsername, keyword).or().like(Users::getEmail, keyword));
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Users::getRecordStatus, status);
        }
        wrapper.orderByDesc(Users::getCreatedAt);

        Page<Users> pageResult = usersMapper.selectPage(new Page<>(page, size), wrapper);
        Page<UserResponse> result = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        result.setRecords(pageResult.getRecords().stream().map(this::convertToResponse).toList());
        return result;
    }

    @Override
    public UserResponse getUserDetail(Long userId) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToResponse(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, String status, String reason, Long adminId, String ipAddress) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRecordStatus(status);
        usersMapper.updateById(user);
        adminLogService.logAction(adminId, "UPDATE_USER_STATUS", "USER", userId,
                "修改用户状态为: " + status + ", 原因: " + reason, ipAddress);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, Long adminId, String ipAddress) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRecordStatus("DELETED");
        user.setDeletedAt(new Date());
        usersMapper.updateById(user);
        adminLogService.logAction(adminId, "DELETE_USER", "USER", userId, "删除用户", ipAddress);
    }

    private UserResponse convertToResponse(Users user) {
        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setEmail(user.getEmail());
        resp.setUsername(user.getUsername());
        resp.setAvatar(user.getAvatar());
        resp.setAnonymousName(user.getAnonymousName());
        resp.setIsAdmin(user.getIsAdmin());
        resp.setRecordStatus(user.getRecordStatus());
        resp.setCreatedAt(user.getCreatedAt());
        resp.setLastLoginAt(user.getLastLoginAt());
        return resp;
    }
}
