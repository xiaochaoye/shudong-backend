package com.chao.shudongbackend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chao.shudongbackend.model.dto.LoginRequestDTO;
import com.chao.shudongbackend.model.dto.RegisterRequestDTO;
import com.chao.shudongbackend.model.dto.Result;
import com.chao.shudongbackend.model.entity.Users;
import com.chao.shudongbackend.service.UsersService;
import com.chao.shudongbackend.utils.JwtUtil;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 发送注册验证码
     * 
     * @param email 邮箱
     * @return 发送结果
     */
    @PostMapping("/register/send-code")
    public Result<Void> sendRegisterCode(@RequestParam String email) {
        try {
            boolean success = usersService.sendRegisterCode(email);
            return success ? Result.success("验证码发送成功") : Result.error("验证码发送失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     * 
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody @Valid RegisterRequestDTO request) {
        try {
            Users user = usersService.register(request);
            return Result.success("注册成功", user.getEmail());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     * 
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            Users user = usersService.login(request);

            // 生成JWT token
            String role = user.getIsAdmin() == 1 ? "R_ADMIN" : "R_USER";
            String token = jwtUtil.generateToken(user.getEmail(), role);

            // 创建响应Map
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            return Result.success("登录成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 发送重置密码验证码
     * 
     * @param email 邮箱
     * @return 发送结果
     */
    @PostMapping("/forgot-password/send-code")
    public Result<Void> sendResetPasswordCode(@RequestParam String email) {
        try {
            boolean success = usersService.sendResetPasswordCode(email);
            return success ? Result.success("验证码发送成功") : Result.error("验证码发送失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 重置密码
     * 
     * @param email       邮箱
     * @param code        验证码
     * @param newPassword 新密码
     * @return 重置结果
     */
    @PostMapping("/forgot-password/reset")
    public Result<Void> resetPassword(@RequestParam String email, @RequestParam String code,
            @RequestParam String newPassword) {
        try {
            boolean success = usersService.resetPassword(email, code, newPassword);
            return success ? Result.success("密码重置成功") : Result.error("密码重置失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
