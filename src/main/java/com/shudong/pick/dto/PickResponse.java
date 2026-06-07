package com.shudong.pick.dto;

import com.shudong.post.dto.PostResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PickResponse {

    private Long pickId;
    private Long postId;
    private String pickType;
    private Date pickedAt;
    private Date resonancedAt;
    private List<PostResponse> posts;
    private Boolean empty;
    private String message;
    private String saying;
}
