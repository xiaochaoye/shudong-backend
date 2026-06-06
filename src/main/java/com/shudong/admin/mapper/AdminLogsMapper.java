package com.shudong.admin.mapper;

import com.shudong.admin.entity.AdminLogs;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【admin_logs(管理员日志表)】的数据库操作Mapper
*/
@Mapper
public interface AdminLogsMapper extends BaseMapper<AdminLogs> {

}




