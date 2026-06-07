package com.shudong.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResonanceRequest {

    @NotNull(message = "帖子ID不能为空")
    private Long postId;

    private String resonanceType;
}
