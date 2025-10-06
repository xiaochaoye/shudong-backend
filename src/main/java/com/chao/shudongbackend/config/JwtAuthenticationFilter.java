package com.chao.shudongbackend.config;

import com.chao.shudongbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            
            if (token != null && jwtUtil.validateToken(token)) {
                // 从令牌中获取用户名
                String username = jwtUtil.getUsernameFromToken(token);
                
                // 从令牌中获取角色信息
                String role = jwtUtil.getRoleFromToken(token);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 创建认证令牌
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + role)
                    );
                    
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                    
                    // 设置认证信息到SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("JWT认证成功 - 用户: {}, 角色: {}", username, role);
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
