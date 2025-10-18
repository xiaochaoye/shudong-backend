package com.chao.shudongbackend.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chao.shudongbackend.model.dto.MailRequestDTO;
import com.chao.shudongbackend.model.dto.Result;
import com.chao.shudongbackend.service.MailService;

import org.springframework.web.bind.annotation.RequestBody;

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
            System.out.println("发送成功");
            return Result.success("邮件发送成功");
        } catch (MailSendException e) {
            System.out.println("失败原因：" + e.getMessage());
            return Result.error("邮件发送失败");
        }
    }
    
}
