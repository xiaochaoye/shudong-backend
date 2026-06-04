package com.shudong.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailRequestDTO {

    @NotBlank(message = "收件人邮箱不为空！")
    @Email(message = "收件人邮箱无合法！")
    private String to;

    @NotBlank(message = "发件人邮箱不为空！")
    @Email(message = "发件人邮箱无合法！")
    private String from;

    @NotBlank(message = "信件内容不为空！")
    private String message;


}
