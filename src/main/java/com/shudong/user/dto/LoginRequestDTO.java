package com.shudong.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 设备唯一标识（可选）
     */
    private String deviceId;

    /**
     * 设备名称（可选）
     */
    private String deviceName;

    /**
     * 用户代理字符串（可选）
     */
    private String userAgent;

    /**
     * IP地址（可选）
     */
    private String ipAddress;
}
