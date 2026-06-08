package com.shudong.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.entity.AdminLogs;
import com.shudong.admin.mapper.AdminLogsMapper;
import com.shudong.admin.service.AdminLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AdminLogServiceImpl implements AdminLogService {

    private final AdminLogsMapper adminLogsMapper;

    @Override
    public void logAction(Long adminId, String action, String targetType, Long targetId, String details, String ipAddress) {
        AdminLogs log = new AdminLogs();
        log.setAdminId(adminId);
        log.setActionType(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setExtraData(details);
        log.setIpAddress(ipAddress);
        log.setCreatedAt(new Date());
        adminLogsMapper.insert(log);
    }

    @Override
    public Page<AdminLogs> getAdminLogs(int page, int size, Long adminId) {
        LambdaQueryWrapper<AdminLogs> wrapper = new LambdaQueryWrapper<>();
        if (adminId != null) {
            wrapper.eq(AdminLogs::getAdminId, adminId);
        }
        wrapper.orderByDesc(AdminLogs::getCreatedAt);
        return adminLogsMapper.selectPage(new Page<>(page, size), wrapper);
    }
}
