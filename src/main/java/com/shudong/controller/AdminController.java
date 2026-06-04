package com.shudong.controller;

import com.shudong.model.dto.Result;
import com.shudong.model.entity.SystemConfig;
import com.shudong.service.AdminStatsService;
import com.shudong.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统管理控制器（管理员接口）
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SystemConfigService systemConfigService;
    private final AdminStatsService adminStatsService;

    /**
     * 获取系统配置列表
     *
     * @return 配置列表
     */
    @GetMapping("/configs")
    public Result<List<SystemConfig>> getConfigs() {
        try {
            List<SystemConfig> configs = systemConfigService.getAllConfigs();
            return Result.success("获取配置列表成功", configs);
        } catch (Exception e) {
            log.error("获取配置列表失败: {}", e.getMessage());
            return Result.error("获取配置列表失败");
        }
    }

    /**
     * 更新系统配置
     *
     * @param params 请求参数，包含 configKey, configValue, configName
     * @return 更新后的配置
     */
    @PostMapping("/configs")
    public Result<SystemConfig> updateConfig(@RequestBody Map<String, String> params) {
        try {
            String configKey = params.get("configKey");
            String configValue = params.get("configValue");
            String configName = params.get("configName");
            if (configKey == null || configValue == null) {
                return Result.error("配置键和值不能为空");
            }
            SystemConfig config = systemConfigService.setConfigValue(configKey, configValue, configName);
            return Result.success("更新配置成功", config);
        } catch (Exception e) {
            log.error("更新配置失败: {}", e.getMessage());
            return Result.error("更新配置失败");
        }
    }

    /**
     * 获取系统概览统计
     *
     * @return 系统概览
     */
    @GetMapping("/stats/overview")
    public Result<Map<String, Object>> getSystemOverview() {
        try {
            Map<String, Object> overview = adminStatsService.getSystemOverview();
            return Result.success("获取系统概览成功", overview);
        } catch (Exception e) {
            log.error("获取系统概览失败: {}", e.getMessage());
            return Result.error("获取系统概览失败");
        }
    }

    /**
     * 获取用户统计
     *
     * @return 用户统计
     */
    @GetMapping("/stats/users")
    public Result<Map<String, Object>> getUserStats() {
        try {
            Map<String, Object> stats = adminStatsService.getUserStats();
            return Result.success("获取用户统计成功", stats);
        } catch (Exception e) {
            log.error("获取用户统计失败: {}", e.getMessage());
            return Result.error("获取用户统计失败");
        }
    }

    /**
     * 获取帖子统计
     *
     * @return 帖子统计
     */
    @GetMapping("/stats/posts")
    public Result<Map<String, Object>> getPostStats() {
        try {
            Map<String, Object> stats = adminStatsService.getPostStats();
            return Result.success("获取帖子统计成功", stats);
        } catch (Exception e) {
            log.error("获取帖子统计失败: {}", e.getMessage());
            return Result.error("获取帖子统计失败");
        }
    }

    /**
     * 获取互动统计
     *
     * @return 互动统计
     */
    @GetMapping("/stats/interactions")
    public Result<Map<String, Object>> getInteractionStats() {
        try {
            Map<String, Object> stats = adminStatsService.getInteractionStats();
            return Result.success("获取互动统计成功", stats);
        } catch (Exception e) {
            log.error("获取互动统计失败: {}", e.getMessage());
            return Result.error("获取互动统计失败");
        }
    }
}
