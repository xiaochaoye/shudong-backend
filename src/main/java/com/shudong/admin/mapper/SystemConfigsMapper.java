package com.shudong.admin.mapper;

import com.shudong.admin.entity.SystemConfigs;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【system_configs(系统配置表)】的数据库操作Mapper
*/
@Mapper
public interface SystemConfigsMapper extends BaseMapper<SystemConfigs> {

}




