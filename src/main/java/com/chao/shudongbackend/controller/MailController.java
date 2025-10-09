package com.chao.shudongbackend.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chao.shudongbackend.model.dto.MailRequestDTO;
import com.chao.shudongbackend.service.MailService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/email")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send_html")
    public String sendHtmlMail(@RequestBody @Valid MailRequestDTO mailRequestDTO) {

        String to = mailRequestDTO.getTo();
        String from = mailRequestDTO.getFrom();
        String message = mailRequestDTO.getMessage();

        try {
            mailService.sendHtmlEmail(to, "来自树洞的回复", message, from);
            System.out.println("发送成功");
            return "邮件发送成功";
        } catch (MailSendException e) {
            System.out.println("失败原因：" + e.getMessage());
            return "邮件发送失败" + e.getMessage();
        }
    }
    
}
