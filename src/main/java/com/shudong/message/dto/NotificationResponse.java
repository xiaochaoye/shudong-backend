package com.shudong.message.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationResponse {

    private Long id;
    private String noticeType;
    private String title;
    private String noticeBody;
    private Integer isRead;
    private Date createdAt;
}
