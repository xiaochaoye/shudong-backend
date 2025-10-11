package com.chao.shudongbackend.utils;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean validatePasswordFormat(String password) {
        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }
        // 正则表达式：至少1个字母，至少1个特殊字符，其余为数字
        String pattern = "^(?=.*[a-zA-Z])(?=.*[\\W_])(?=.*\\d)[a-zA-Z\\d\\W_]{8,20}$";
        return password.matches(pattern);
    }

    /**
     * 加密密码
     * 
     * @param password 原始密码
     * @return 加密后的密码
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 验证密码
     * 
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 生成随机验证码（6位数字）
     * 
     * @return 验证码
     */
    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(900000) + 100000);
    }

    /**
     * 生成随机用户名（“用户” + 邮箱前缀）
     * 
     * @param email 邮箱
     * @return 用户名
     */
    public String generateUsername(String email) {
        String emailPrefix = email.split("@")[0];
        return "用户" + emailPrefix;
    }
}
