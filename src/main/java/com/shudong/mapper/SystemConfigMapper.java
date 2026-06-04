package com.shudong.mapper;

import com.shudong.model.entity.SystemConfig;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author test
 * @description 针对表【system_configs(系统配置表)】的数据库操作Mapper
 * @createDate 2025-10-05 23:04:29
 * @Entity com.shudong.model.entity.SystemConfig
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

}
