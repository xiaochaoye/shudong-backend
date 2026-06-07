package com.shudong.post.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId;
    private String commentBody;
    private Date createdAt;
}
