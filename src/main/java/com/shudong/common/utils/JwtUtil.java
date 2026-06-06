package com.shudong.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * <p>
 * 提供JWT令牌的生成、验证和解析功能
 * </p>
 * 
 * @author chao
 * @version 1.0
 * @since 2025-10-06
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret:shudong-backend-secret-key-2025}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration; // 默认24小时

    @Value("${jwt.header:Authorization}")
    private String header;

    @Value("${jwt.prefix:Bearer }")
    private String prefix;

    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final String CLAIM_TYPE = "type";

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    /**
     * 生成JWT令牌
     *
     * @param email 用户邮箱（唯一）
     * @param role  用户角色
     * @return JWT令牌
     */
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put(CLAIM_TYPE, TOKEN_TYPE_ACCESS);
        return createToken(claims, email, expiration);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从令牌中获取角色
     *
     * @param token JWT令牌
     * @return 用户角色
     */
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            log.warn("JWT令牌验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建JWT令牌
     *
     * @param claims  声明信息
     * @param subject 主题（用户名）
     * @return JWT令牌
     */
    private String createToken(Map<String, Object> claims, String subject, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * 从令牌中获取声明信息
     *
     * @param token JWT令牌
     * @return 声明信息
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 检查令牌是否即将过期（在指定时间内）
     *
     * @param token   JWT令牌
     * @param minutes 分钟数
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token, int minutes) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long diff = expiration.getTime() - now.getTime();
            return diff <= (minutes * 60 * 1000);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成刷新令牌
     *
     * @param email 用户邮箱（唯一）
     * @param role  用户角色
     * @return 刷新令牌
     */
    public String generateRefreshToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put(CLAIM_TYPE, TOKEN_TYPE_REFRESH);
        return createToken(claims, email, refreshExpiration);
    }

    /**
     * 判断令牌是否为刷新令牌
     *
     * @param token JWT令牌
     * @return 是否为刷新令牌
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return TOKEN_TYPE_REFRESH.equals(claims.get(CLAIM_TYPE, String.class));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取刷新令牌过期时间
     *
     * @return 刷新令牌过期时间（毫秒）
     */
    public Long getRefreshExpiration() {
        return refreshExpiration;
    }

    /**
     * 获取JWT令牌的请求头名称
     *
     * @return 请求头名称
     */
    public String getHeader() {
        return header;
    }

    /**
     * 获取JWT令牌的前缀
     *
     * @return 令牌前缀
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 获取JWT令牌过期时间
     * @param token
     * @return
     */
    public Date getExpirationFromToken(String token) {
        try {
            return getClaimsFromToken(token).getExpiration();
        } catch (Exception e) {
            log.warn("获取令牌过期时间失败: {}", e.getMessage());
            throw new RuntimeException("令牌解析失败");
        }
    }

}
