package com.chao.shudongbackend.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 更新愿望请求DTO
 */
@Data
public class WishUpdateDTO {

    /**
     * 愿望标题
     */
    @NotBlank(message = "愿望标题不能为空")
    @Size(max = 200, message = "愿望标题长度不能超过200个字符")
    private String title;

    /**
     * 愿望内容
     */
    @NotBlank(message = "愿望内容不能为空")
    private String content;

    /**
     * 是否匿名发布
     */
    @NotNull(message = "是否匿名不能为空")
    private Boolean isAnonymous;
}
