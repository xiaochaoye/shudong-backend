package com.shudong.post.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PostResponse {

    private Long id;
    private String title;
    private String postBody;
    private Integer isAnonymous;
    private Integer isPrivate;
    private Integer viewCount;
    private Integer resonanceCount;
    private Integer commentCount;
    private Date createdAt;
}
