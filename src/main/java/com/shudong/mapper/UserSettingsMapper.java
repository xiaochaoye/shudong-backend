package com.shudong.mapper;

import com.shudong.model.entity.UserSettings;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【user_settings(用户设置表，存储用户个性化设置)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:30
* @Entity com.shudong.model.entity.UserSettings
*/
@Mapper
public interface UserSettingsMapper extends BaseMapper<UserSettings> {

}
