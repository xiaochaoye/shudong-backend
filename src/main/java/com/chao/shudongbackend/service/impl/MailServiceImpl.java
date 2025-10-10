package com.chao.shudongbackend.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.chao.shudongbackend.service.MailService;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent, String from) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            Context context = new Context();
            context.setVariable("from", from);
            context.setVariable("message", htmlContent); // 传入用户自定义内容

            String processedHtml = buildEmailContent("your_reply", context);

            // 设置发件人邮箱地址
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(processedHtml, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送邮件失败" + e.getMessage(), e);
        }
    }

    // private String buildEmailContent(String message, String from) {
    // Context context = new Context();
    // context.setVariable("from", from);
    // context.setVariable("message", message); // 传入用户自定义内容
    // return templateEngine.process("your_reply", context);
    // }

    @Override
    public void sendVerificationEmail(String to, String subject, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            Context context = new Context();
            context.setVariable("verificationCode", code); // 传入用户自定义内容
            String processedHtml = buildEmailContent("verification_email", context);

            // 设置发件人邮箱地址
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(processedHtml, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送邮件失败" + e.getMessage(), e);
        }
    }

    private String buildEmailContent(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }
}
