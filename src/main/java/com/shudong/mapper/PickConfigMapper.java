package com.shudong.mapper;

import com.shudong.model.entity.PickConfig;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author test
 * @description 针对表【pick_configs(拾取配置表)】的数据库操作Mapper
 * @createDate 2025-10-05 23:04:29
 * @Entity com.shudong.model.entity.PickConfig
 */
@Mapper
public interface PickConfigMapper extends BaseMapper<PickConfig> {

}
