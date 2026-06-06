package com.shudong.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.user.entity.Users;

/**
* @author test
* @description 针对表【users(用户表，存储注册用户信息)】的数据库操作Mapper
* 
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




