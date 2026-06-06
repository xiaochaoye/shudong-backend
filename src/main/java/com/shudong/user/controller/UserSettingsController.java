package com.shudong.user.controller;

import com.shudong.common.response.Result;
import com.shudong.user.entity.UserSettings;
import com.shudong.user.service.UserSettingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户设置管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/user-settings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    /**
     * 获取当前用户设置
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @return 用户设置
     */
    @GetMapping
    public Result<UserSettings> getUserSettings(@RequestAttribute("userId") Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            UserSettings settings = userSettingsService.getByUserId(userId);
            if (settings == null) {
                settings = userSettingsService.createDefaultSettings(userId);
            }
            return Result.success(settings);
        } catch (Exception e) {
            log.error("获取用户设置失败: {}", e.getMessage());
            return Result.error("获取用户设置失败");
        }
    }

    /**
     * 更新用户设置
     *
     * @param userId    当前用户ID（从JWT token解析）
     * @param settings  用户设置
     * @return 更新结果
     */
    @PutMapping
    public Result<Void> updateUserSettings(@RequestAttribute("userId") Long userId,
                                           @RequestBody UserSettings settings) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            userSettingsService.updateByUserId(userId, settings);
            return Result.success("用户设置更新成功");
        } catch (Exception e) {
            log.error("更新用户设置失败: {}", e.getMessage());
            return Result.error("更新用户设置失败");
        }
    }

    /**
     * 重置为默认设置
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @return 重置结果
     */
    @PostMapping("/reset")
    public Result<UserSettings> resetUserSettings(@RequestAttribute("userId") Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            UserSettings settings = userSettingsService.createDefaultSettings(userId);
            return Result.success("已重置为默认设置", settings);
        } catch (Exception e) {
            log.error("重置用户设置失败: {}", e.getMessage());
            return Result.error("重置用户设置失败");
        }
    }
}
