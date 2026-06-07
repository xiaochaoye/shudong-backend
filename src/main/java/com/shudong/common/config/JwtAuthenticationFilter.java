package com.shudong.common.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shudong.user.entity.Users;
import com.shudong.user.service.UsersService;

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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);

            if (token != null) {
                if (redisUtil.isInBlacklist(token)) {
                    log.warn("JWT令牌在黑名单中，拒绝访问 - 令牌: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                    filterChain.doFilter(request, response);
                    return;
                }

                if (jwtUtil.validateToken(token)) {
                    String userEmail = jwtUtil.getEmailFromToken(token);
                    String role = jwtUtil.getRoleFromToken(token);

                    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + role)
                        );

                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userEmail, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 通过 email 查询 userId 并设置到 request attribute
                        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("email", userEmail);
                        Users user = usersService.getOne(queryWrapper);
                        if (user != null) {
                            request.setAttribute("userId", user.getId());
                        }

                        log.debug("JWT认证成功 - 用户邮件地址: {}, 角色: {}", userEmail, role);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JWT认证失败: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtUtil.getHeader());
        if (bearerToken != null && bearerToken.startsWith(jwtUtil.getPrefix())) {
            return bearerToken.substring(jwtUtil.getPrefix().length());
        }
        return null;
    }
}
