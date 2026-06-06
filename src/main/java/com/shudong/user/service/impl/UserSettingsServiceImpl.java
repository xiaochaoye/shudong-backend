package com.shudong.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.user.entity.UserSettings;
import com.shudong.user.mapper.UserSettingsMapper;
import com.shudong.user.service.UserSettingsService;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author test
 * @description 针对表【user_settings(用户设置表，存储用户个性化设置)】的数据库操作Service实现
 */
@Service
public class UserSettingsServiceImpl extends ServiceImpl<UserSettingsMapper, UserSettings>
    implements UserSettingsService {

    @Override
    public UserSettings getByUserId(Long userId) {
        QueryWrapper<UserSettings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public void updateByUserId(Long userId, UserSettings settings) {
        QueryWrapper<UserSettings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        settings.setUpdatedAt(new Date());
        this.update(settings, queryWrapper);
    }

    @Override
    public UserSettings createDefaultSettings(Long userId) {
        UserSettings settings = new UserSettings();
        settings.setUserId(userId);
        settings.setEmailNotifications(1);
        settings.setPushNotifications(1);
        settings.setAiAnalysisEnabled(1);
        settings.setDailyPickLimit(20);
        settings.setNightPickLimit(40);
        settings.setUpdatedAt(new Date());
        this.save(settings);
        return settings;
    }
}
