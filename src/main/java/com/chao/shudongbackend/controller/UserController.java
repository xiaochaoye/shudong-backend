package com.chao.shudongbackend.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.chao.shudongbackend.model.dto.LoginRequestDTO;
import com.chao.shudongbackend.model.dto.RegisterRequestDTO;
import com.chao.shudongbackend.model.dto.Result;
import com.chao.shudongbackend.model.entity.Users;
import com.chao.shudongbackend.service.UsersService;
import com.chao.shudongbackend.utils.JwtUtil;
import com.chao.shudongbackend.utils.RedisUtil;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HttpServletRequest request;

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

    /**
     * 用户登出
     * 
     * <p>
     * 将当前JWT令牌加入黑名单，使其失效
     * </p>
     * 
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        try {
            // 从请求头中获取JWT令牌
            String token = getJwtFromRequest();

            if (token == null) {
                return Result.error("未找到有效的JWT令牌");
            }

            // 验证令牌有效性
            if (!jwtUtil.validateToken(token)) {
                return Result.error("无效的JWT令牌");
            }

            // 计算令牌剩余过期时间（秒）
            long remainingTime = calculateTokenRemainingTime(token);

            // 将令牌加入黑名单，设置与令牌剩余时间相同的TTL
            redisUtil.addToBlacklist(token, remainingTime);

            log.info("用户登出成功 - 令牌已加入黑名单，剩余时间: {}秒", remainingTime);
            return Result.success("登出成功");

        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage());
            return Result.error("登出失败: " + e.getMessage());
        }
    }

    /**
     * 从HTTP请求中提取JWT令牌
     * 
     * @return JWT令牌，如果不存在则返回null
     */
    private String getJwtFromRequest() {
        String bearerToken = request.getHeader(jwtUtil.getHeader());
        if (bearerToken != null && bearerToken.startsWith(jwtUtil.getPrefix())) {
            return bearerToken.substring(jwtUtil.getPrefix().length());
        }
        return null;
    }

    /**
     * 计算JWT令牌的剩余过期时间（秒）
     * 
     * @param token JWT令牌
     * @return 剩余过期时间（秒）
     */
    private long calculateTokenRemainingTime(String token) {
        try {
            Date expiration = jwtUtil.getExpirationFromToken(token);
            Date now = new Date();

            // 计算剩余时间（毫秒转秒）
            long remainingTimeMillis = expiration.getTime() - now.getTime();
            long remainingTimeSeconds = remainingTimeMillis / 1000;

            // 这里简化处理，使用固定的剩余时间
            // 实际应该从JWT令牌中解析exp字段
            return Math.max(remainingTimeSeconds, 0);

        } catch (Exception e) {
            log.warn("计算令牌剩余时间失败，使用默认过期时间: {}", e.getMessage());
            // 如果计算失败，使用默认的1小时过期时间
            return 3600;
        }
    }
}
