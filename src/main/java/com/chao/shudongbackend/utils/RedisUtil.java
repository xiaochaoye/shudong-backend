package com.chao.shudongbackend.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 设置缓存
     * 
     * @param key   键
     * @param value 值
     * @param time  过期时间（秒）
     */
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     * 
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     * 
     * @param key 键
     * @return 是否删除成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置验证码
     * 
     * @param email 邮箱
     * @param code  验证码
     * @param type  类型（register/reset）
     */
    public void setVerificationCode(String email, String code, String type) {
        String key = type + "_code:" + email;
        set(key, code, 300); // 5分钟过期
    }

    /**
     * 获取验证码
     * 
     * @param email 邮箱
     * @param type  类型（register/reset）
     * @return 验证码
     */
    public String getVerificationCode(String email, String type) {
        String key = type + "_code:" + email;
        Object code = get(key);
        return code != null ? code.toString() : null;
    }

    /**
     * 验证验证码
     * 
     * @param email 邮箱
     * @param code  验证码
     * @param type  类型（register/reset）
     * @return 是否验证成功
     */
    public boolean verifyCode(String email, String code, String type) {
        String storedCode = getVerificationCode(email, type);
        return storedCode != null && storedCode.equals(code);
    }

    /**
     * 删除验证码
     * 
     * @param email 邮箱
     * @param type  类型（register/reset）
     */
    public void deleteVerificationCode(String email, String type) {
        String key = type + "_code:" + email;
        delete(key);
    }

    /**
     * 将令牌加入黑名单
     * 
     * @param token          JWT令牌
     * @param expirationTime 过期时间（秒）
     */
    public void addToBlacklist(String token, long expirationTime) {
        String key = "blacklist:" + token;
        set(key, "revoked", expirationTime);
    }

    /**
     * 检查令牌是否在黑名单中
     * 
     * @param token JWT令牌
     * @return 是否在黑名单中
     */
    public boolean isInBlacklist(String token) {
        String key = "blacklist:" + token;
        return hasKey(key);
    }

    /**
     * 从黑名单中移除令牌（用于清理）
     * 
     * @param token JWT令牌
     */
    public void removeFromBlacklist(String token) {
        String key = "blacklist:" + token;
        delete(key);
    }

    /**
     * 原子递增
     * 
     * @param key   键
     * @param delta 增量（通常为1）
     * @return 递增后的值
     */
    public Long incr(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 原子递减
     * 
     * @param key   键
     * @param delta 减量（通常为1）
     * @return 递减后的值
     */
    public Long decr(String key, long delta) {
        return stringRedisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 设置过期时间（秒）
     * 
     * @param key     键
     * @param seconds 过期时间（秒）
     */
    public void expire(String key, long seconds) {
        stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }
}
