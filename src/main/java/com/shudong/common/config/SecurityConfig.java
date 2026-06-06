package com.shudong.common.config;

import com.shudong.common.exception.JwtAccessDeniedHandler;
import com.shudong.common.utils.JwtUtil;
import com.shudong.common.utils.RedisUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;

    private final SecurityProperties securityProperties;

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
            .csrf(csrf -> csrf.disable())
            // 启用CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 配置会话管理为无状态
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求授权
            .authorizeHttpRequests(auth -> auth
                // 公开访问的接口
                .requestMatchers(
                    securityProperties.getPublicEndpoints().toArray(String[]::new)
                ).permitAll()
                // 管理员接口
                .requestMatchers(
                    securityProperties.getAdminEndpoints().toArray(String[]::new)
                ).hasRole("ADMIN")
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
            // 添加JWT认证过滤器
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, redisUtil), UsernamePasswordAuthenticationFilter.class)
            // 配置异常处理
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
            );

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
