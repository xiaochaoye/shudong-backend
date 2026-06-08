package com.shudong.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shudong.common.response.Result;
import com.shudong.common.utils.CookieUtil;
import com.shudong.common.utils.JwtUtil;
import com.shudong.common.utils.RedisUtil;
import com.shudong.user.dto.LoginRequestDTO;
import com.shudong.user.dto.RegisterRequestDTO;
import com.shudong.user.dto.UserRequestDTO;
import com.shudong.user.entity.Users;
import com.shudong.user.service.UsersService;
import com.shudong.user.vo.UserVO;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor // 构造器注入，自动生成 final 字段的构造函数
public class UserController {

    private final UsersService usersService;

    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;

    private final CookieUtil cookieUtil;

    private final HttpServletRequest request;

    /**
     * 发送注册验证码
     * 
     * @param email 邮箱
     * @return 发送结果
     */
    @PostMapping("/register/send-code")
    public Result<Void> sendRegisterCode(@RequestParam(name = "email") String email) {
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
    public Result<UserVO> register(@RequestBody @Valid RegisterRequestDTO request) {
        try {
            Users user = usersService.register(request);
            UserVO userVO = convertToUserVO(user);
            return Result.success("注册成功", userVO);
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
    public Result<Map<String, Object>> login(@RequestBody @Valid LoginRequestDTO request,
                                              HttpServletResponse response) {
        try {
            Users user = usersService.login(request);

            // 生成JWT token
            String role = user.getIsAdmin() == 1 ? "ADMIN" : "USER";
            String token = jwtUtil.generateToken(user.getEmail(), role);

            // 生成 Refresh Token
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), role);

            // 存储 Refresh Token 到 Redis（7天）
            redisUtil.setRefreshToken(user.getEmail(), refreshToken, jwtUtil.getRefreshExpiration() / 1000);

            // 通过 HttpOnly Cookie 返回 Refresh Token
            cookieUtil.addRefreshTokenCookie(response, refreshToken);

            // 创建响应Map
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            UserVO userVO = convertToUserVO(user);
            responseData.put("user", userVO);

            return Result.success("登录成功", responseData);
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
    public Result<Void> sendResetPasswordCode(@RequestParam(name = "email") String email) {
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
    public Result<Void> resetPassword(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code,
            @RequestParam(name = "newPassword") String newPassword) {
        try {
            boolean success = usersService.resetPassword(email, code, newPassword);
            return success ? Result.success("密码重置成功") : Result.error("密码重置失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户资料（名称和头像）
     * 
     * @param request 更新请求，包含用户名和头像文件
     * @return 更新结果
     */
    @PostMapping("/profile/update")
    public Result<Map<String, Object>> updateProfile(@ModelAttribute @Valid UserRequestDTO request) {
        try {
            // 从Spring Security认证信息中获取当前用户邮箱
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Result.error("用户未登录");
            }
            
            String email = authentication.getName();

            // 通过邮箱查询用户
            QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            Users currentUser = usersService.getOne(queryWrapper);
            if (currentUser == null) {
                return Result.error("用户不存在");
            }

            // 调用服务更新资料
            Users updatedUser = usersService.updateProfile(currentUser.getId(), request);

            Map<String, Object> data = new HashMap<>();
            data.put("username", updatedUser.getUsername());
            data.put("avatarUrl", updatedUser.getAvatar());

            return Result.success("资料更新成功", data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     * 
     * 将当前JWT令牌加入黑名单，使其失效
     * 
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletResponse response) {
        try {
            // 从请求头中获取JWT令牌（登出时需要令牌本身加入黑名单）
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

            // 删除 Redis 中的 Refresh Token
            String email = jwtUtil.getEmailFromToken(token);
            redisUtil.deleteRefreshToken(email);

            // 清除 HttpOnly Cookie
            cookieUtil.clearRefreshTokenCookie(response);

            log.info("用户登出成功 - 令牌已加入黑名单，剩余时间: {}秒", remainingTime);
            return Result.success("登出成功");

        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage());
            return Result.error("登出失败: " + e.getMessage());
        }
    }

    /**
     * 刷新 Access Token
     */
    @PostMapping("/refresh")
    public Result<Map<String, Object>> refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 从 Cookie 中获取 Refresh Token
            String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
            if (refreshToken == null) {
                return Result.error(401, "缺少 Refresh Token");
            }

            // 验证 JWT 签名和有效期
            if (!jwtUtil.validateToken(refreshToken)) {
                cookieUtil.clearRefreshTokenCookie(response);
                return Result.error(401, "Refresh Token 无效或已过期");
            }

            // 确认是 Refresh Token 类型
            if (!jwtUtil.isRefreshToken(refreshToken)) {
                cookieUtil.clearRefreshTokenCookie(response);
                return Result.error(401, "无效的 Token 类型");
            }

            // 从 Token 中提取用户信息
            String email = jwtUtil.getEmailFromToken(refreshToken);
            String role = jwtUtil.getRoleFromToken(refreshToken);

            // 校验 Redis 中存储的 Refresh Token 是否匹配
            if (!redisUtil.validateRefreshToken(email, refreshToken)) {
                // Token 不匹配，说明可能被盗用（复用已轮转的旧 Token）
                redisUtil.deleteRefreshToken(email);
                cookieUtil.clearRefreshTokenCookie(response);
                log.warn("检测到 Refresh Token 复用，已吊销该用户所有 Refresh Token - email: {}", email);
                return Result.error(401, "Refresh Token 已失效，请重新登录");
            }

            // 签发新的 Access Token
            String newAccessToken = jwtUtil.generateToken(email, role);

            // 签发新的 Refresh Token（轮转）
            String newRefreshToken = jwtUtil.generateRefreshToken(email, role);

            // 更新 Redis 中的 Refresh Token
            redisUtil.setRefreshToken(email, newRefreshToken, jwtUtil.getRefreshExpiration() / 1000);

            // 设置新的 Cookie
            cookieUtil.addRefreshTokenCookie(response, newRefreshToken);

            // 返回新的 Access Token
            Map<String, Object> data = new HashMap<>();
            data.put("token", newAccessToken);

            return Result.success("刷新成功", data);
        } catch (Exception e) {
            log.error("刷新 Token 失败: {}", e.getMessage());
            cookieUtil.clearRefreshTokenCookie(response);
            return Result.error(401, "刷新失败，请重新登录");
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

            // 从JWT令牌中解析exp字段，动态计算剩余时间
            return Math.max(remainingTimeSeconds, 0);

        } catch (Exception e) {
            log.warn("计算令牌剩余时间失败，使用默认过期时间: {}", e.getMessage());
            // 如果计算失败，使用默认的1小时过期时间
            return 3600;
        }
    }

    /**
     * 将Users实体转换为UserVO视图对象
     *
     * @param user 用户实体
     * @return 用户视图对象
     */
    private UserVO convertToUserVO(Users user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setEmail(user.getEmail());
        userVO.setUsername(user.getUsername());
        userVO.setAvatar(user.getAvatar());
        userVO.setAnonymousName(user.getAnonymousName());
        userVO.setAnonymousAvatar(user.getAnonymousAvatar());
        userVO.setIsAdmin(user.getIsAdmin());
        userVO.setRecordStatus(user.getRecordStatus());
        userVO.setCreatedAt(user.getCreatedAt());
        userVO.setLastLoginAt(user.getLastLoginAt());
        return userVO;
    }

}
