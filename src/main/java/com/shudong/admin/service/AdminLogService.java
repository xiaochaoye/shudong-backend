package com.shudong.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.entity.AdminLogs;

public interface AdminLogService {

    void logAction(Long adminId, String action, String targetType, Long targetId, String details, String ipAddress);

    Page<AdminLogs> getAdminLogs(int page, int size, Long adminId);
}
