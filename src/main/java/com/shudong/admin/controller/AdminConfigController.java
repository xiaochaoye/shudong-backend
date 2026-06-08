package com.shudong.admin.controller;

import com.shudong.admin.entity.SystemConfigs;
import com.shudong.admin.mapper.SystemConfigsMapper;
import com.shudong.admin.service.AdminLogService;
import com.shudong.common.exception.BusinessException;
import com.shudong.common.response.Result;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/configs")
@RequiredArgsConstructor
public class AdminConfigController {

    private final SystemConfigsMapper systemConfigsMapper;
    private final UsersMapper usersMapper;
    private final AdminLogService adminLogService;

    @GetMapping
    public Result<List<SystemConfigs>> getAllConfigs() {
        return Result.success(systemConfigsMapper.selectList(null));
    }

    @PutMapping("/{id}")
    public Result<Void> updateConfig(@PathVariable Long id, @RequestBody ConfigUpdateRequest request,
                                     HttpServletRequest httpRequest) {
        SystemConfigs config = systemConfigsMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("配置项不存在");
        }
        config.setConfigValue(request.getValue());
        config.setUpdatedAt(new Date());
        systemConfigsMapper.updateById(config);
        adminLogService.logAction(getCurrentAdminId(), "UPDATE_CONFIG", "SYSTEM_CONFIG", id,
                "更新配置: " + config.getConfigKey() + " = " + request.getValue(), getClientIp(httpRequest));
        return Result.success();
    }

    @Data
    public static class ConfigUpdateRequest {
        private String value;
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
