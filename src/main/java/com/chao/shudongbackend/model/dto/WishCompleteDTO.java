package com.chao.shudongbackend.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 完成愿望请求DTO
 */
@Data
public class WishCompleteDTO {

    /**
     * 愿望ID
     */
    @NotNull(message = "愿望ID不能为空")
    private Long wishId;
}
