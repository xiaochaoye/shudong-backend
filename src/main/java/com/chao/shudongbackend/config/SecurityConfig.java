package com.chao.shudongbackend.config;

import com.chao.shudongbackend.exception.JwtAccessDeniedHandler;
import com.chao.shudongbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 配置类
 * 
 * <p>配置应用程序的安全策略，包括认证、授权、CORS等</p>
 * 
 * @author chao
 * @version 1.0
 * @since 2025-10-06
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    /**
     * 密码编码器
     *
     * @return BCrypt密码编码器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤器链配置
     *
     * @param http HTTP安全配置
     * @return 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护（因为使用JWT）
            .csrf().disable()
            // 启用CORS
            .cors().and()
            // 配置会话管理为无状态
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            // 配置请求授权
            .authorizeRequests()
                // 公开访问的接口
                .antMatchers(
                    "/api/auth/**",           // 认证相关接口
                    "/api/users/register",    // 用户注册
                    "/api/users/login",       // 用户登录
                    "/api/posts/public/**",   // 公开帖子接口
                    "/api/comments/public/**", // 公开评论接口
                    "/doc.html",              // Knife4j文档
                    "/webjars/**",            // Knife4j静态资源
                    "/v3/api-docs/**",        // OpenAPI文档
                    "/favicon.ico",           // 网站图标
                    "/error"                  // 错误页面
                ).permitAll()
                // 管理员接口
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                // 其他所有请求都需要认证
                .anyRequest().authenticated().and()
            // 添加JWT认证过滤器
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            // 配置异常处理
            .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        return http.build();
    }

    /**
     * CORS配置源
     *
     * @return CORS配置源实例
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
