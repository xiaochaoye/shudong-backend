package com.shudong.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shudong.common.utils.JwtUtil;
import com.shudong.common.utils.RedisUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * JWT认证过滤器
 * 
 * <p>拦截请求并验证JWT令牌，设置认证信息到SecurityContext</p>
 * 
 * @author chao
 * @version 1.0
 * @since 2025-10-06
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    /**
     * 执行JWT令牌验证
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            // 从请求头获取JWT令牌
            String token = getJwtFromRequest(request);
            
            // 检查令牌是否存在且有效
            if (token != null) {
                // 首先检查令牌是否在黑名单中（用户已登出）
                if (redisUtil.isInBlacklist(token)) {
                    log.warn("JWT令牌在黑名单中，拒绝访问 - 令牌: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                    // 继续过滤器链，由后续的异常处理器处理认证失败
                    filterChain.doFilter(request, response);
                    return;
                }
                
                // 验证令牌的有效性
                if (jwtUtil.validateToken(token)) {
                    // 从令牌中获取用户邮件地址
                    String userEmail = jwtUtil.getEmailFromToken(token);
                    
                    // 从令牌中获取角色信息
                    String role = jwtUtil.getRoleFromToken(token);
                    
                    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 创建认证令牌
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + role)
                        );
                        
                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
                        
                        // 设置认证信息到SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        log.debug("JWT认证成功 - 用户邮件地址: {}, 角色: {}", userEmail, role);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JWT认证失败: {}", e.getMessage());
            // 认证失败，继续过滤器链，由后续的异常处理器处理
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 从HTTP请求中提取JWT令牌
     *
     * @param request HTTP请求
     * @return JWT令牌，如果不存在则返回null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtUtil.getHeader());
        if (bearerToken != null && bearerToken.startsWith(jwtUtil.getPrefix())) {
            return bearerToken.substring(jwtUtil.getPrefix().length());
        }
        return null;
    }
}
