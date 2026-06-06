package com.shudong.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.user.entity.UserSettings;

/**
* @author test
* @description 针对表【user_settings(用户设置表，存储用户个性化设置)】的数据库操作Mapper
* 
*/
@Mapper
public interface UserSettingsMapper extends BaseMapper<UserSettings> {

}
