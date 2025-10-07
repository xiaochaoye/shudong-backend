package com.chao.shudongbackend.mapper;

import com.chao.shudongbackend.model.entity.Users;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【users(用户表，存储注册用户信息)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:30
* @Entity com.chao.shudongbackend.model.entity.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




