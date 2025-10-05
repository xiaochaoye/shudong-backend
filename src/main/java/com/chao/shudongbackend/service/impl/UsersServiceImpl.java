package com.chao.shudongbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.shudongbackend.model.entity.Users;
import com.chao.shudongbackend.service.UsersService;
import com.chao.shudongbackend.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【users(用户表，存储注册用户信息)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:30
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




