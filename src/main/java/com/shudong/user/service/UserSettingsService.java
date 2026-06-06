package com.shudong.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.user.entity.UserSettings;

/**
 * @author test
 * @description 针对表【user_settings(用户设置表，存储用户个性化设置)】的数据库操作Service
 */
public interface UserSettingsService extends IService<UserSettings> {

    /**
     * 根据用户ID获取设置
     * @param userId 用户ID
     * @return 用户设置
     */
    UserSettings getByUserId(Long userId);

    /**
     * 更新用户设置
     * @param userId 用户ID
     * @param settings 用户设置
     */
    void updateByUserId(Long userId, UserSettings settings);

    /**
     * 创建默认设置
     * @param userId 用户ID
     * @return 创建的用户设置
     */
    UserSettings createDefaultSettings(Long userId);
}
