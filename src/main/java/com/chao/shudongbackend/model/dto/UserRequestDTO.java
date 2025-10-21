package com.chao.shudongbackend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserRequestDTO {

    /**
     * 用户名，可选更新
     */
    @Size(min = 1, max = 50, message = "用户名长度必须在1-50个字符之间")
    private String username;

    /**
     * 头像文件，可选更新
     */
    private MultipartFile avatarFile;

}
