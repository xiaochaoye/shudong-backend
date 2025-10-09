package com.chao.shudongbackend.service;

import com.chao.shudongbackend.model.dto.LoginRequestDTO;
import com.chao.shudongbackend.model.dto.RegisterRequestDTO;
import com.chao.shudongbackend.model.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author test
* @description 针对表【users(用户表，存储注册用户信息)】的数据库操作Service
* @createDate 2025-10-05 23:04:30
*/
public interface UsersService extends IService<Users> {

    /**
     * 发送注册验证码
     * @param email 邮箱
     * @return 是否发送成功
     */
    boolean sendRegisterCode(String email);

    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册成功的用户信息
     */
    Users register(RegisterRequestDTO request);

    /**
     * 用户登录
     * @param request 登录请求
     * @return 登录成功的用户信息
     */
    Users login(LoginRequestDTO request);

    /**
     * 发送重置密码验证码
     * @param email 邮箱
     * @return 是否发送成功
     */
    boolean sendResetPasswordCode(String email);

    /**
     * 重置密码
     * @param email 邮箱
     * @param code 验证码
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(String email, String code, String newPassword);
}
