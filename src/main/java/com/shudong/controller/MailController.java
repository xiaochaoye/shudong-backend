package com.shudong.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shudong.model.dto.MailRequestDTO;
import com.shudong.model.dto.Result;
import com.shudong.service.MailService;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/email")
public class MailController {

    // 构造器注入，不使用注解版
    private final MailService mailService;

    @Autowired
    public MailController (MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send_html")
    public Result<Void> sendHtmlMail(@RequestBody @Valid MailRequestDTO mailRequestDTO) {

        String to = mailRequestDTO.getTo();
        String from = mailRequestDTO.getFrom();
        String message = mailRequestDTO.getMessage();

        try {
            mailService.sendHtmlEmail(to, "来自树洞的回复", message, from);
            log.info("邮件发送成功");
            return Result.success("邮件发送成功");
        } catch (MailSendException e) {
            log.error("邮件发送失败，原因：{}", e.getMessage(), e);
            return Result.error("邮件发送失败");
        }
    }
    
}
