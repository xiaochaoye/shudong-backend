package com.shudong.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudong.common.response.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT访问拒绝处理器
 * 
 * <p>处理权限不足的情况，返回统一的403响应</p>
 * 
 * @author chao
 * @version 1.0
 * @since 2025-10-06
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理访问拒绝
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param accessDeniedException 访问拒绝异常
     * @throws IOException IO异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException {
        log.warn("权限不足 - 请求路径: {}, 异常信息: {}", request.getRequestURI(), accessDeniedException.getMessage());
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        
        Result<Object> result = Result.forbidden();
        String jsonResponse = objectMapper.writeValueAsString(result);
        
        response.getWriter().write(jsonResponse);
    }
}
