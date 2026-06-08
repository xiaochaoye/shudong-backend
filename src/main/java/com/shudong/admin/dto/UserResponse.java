package com.shudong.admin.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String avatar;
    private String anonymousName;
    private Integer isAdmin;
    private String recordStatus;
    private java.util.Date createdAt;
    private java.util.Date lastLoginAt;
}
