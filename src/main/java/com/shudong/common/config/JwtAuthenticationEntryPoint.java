package com.shudong.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudong.common.response.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证入口点
 * 
 * <p>处理认证失败的情况，返回统一的401响应</p>
 * 
 * @author chao
 * @version 1.0
 * @since 2025-10-06
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理认证失败
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param authException 认证异常
     * @throws IOException IO异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        log.warn("认证失败 - 请求路径: {}, 异常信息: {}", request.getRequestURI(), authException.getMessage());
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        Result<Object> result = Result.unauthorized();
        String jsonResponse = objectMapper.writeValueAsString(result);
        
        response.getWriter().write(jsonResponse);
    }
}
