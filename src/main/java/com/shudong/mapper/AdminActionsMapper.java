package com.shudong.mapper;

import com.shudong.model.entity.AdminActions;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【admin_actions(管理员操作日志表，用于审计)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:29
* @Entity com.chao.shudongbackend.model.entity.AdminActions
*/
@Mapper
public interface AdminActionsMapper extends BaseMapper<AdminActions> {

}




