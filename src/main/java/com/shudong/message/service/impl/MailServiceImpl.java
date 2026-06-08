package com.shudong.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shudong.message.entity.EmailTemplates;
import com.shudong.message.entity.PrivateReplies;
import com.shudong.message.mapper.EmailTemplatesMapper;
import com.shudong.message.mapper.PrivateRepliesMapper;
import com.shudong.message.service.MailService;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Date;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine stringTemplateEngine;
    private final EmailTemplatesMapper emailTemplatesMapper;
    private final PrivateRepliesMapper privateRepliesMapper;
    private final UsersMapper usersMapper;

    public MailServiceImpl(JavaMailSender javaMailSender,
                           TemplateEngine templateEngine,
                           EmailTemplatesMapper emailTemplatesMapper,
                           PrivateRepliesMapper privateRepliesMapper,
                           UsersMapper usersMapper) {
        this.javaMailSender = javaMailSender;
        this.emailTemplatesMapper = emailTemplatesMapper;
        this.privateRepliesMapper = privateRepliesMapper;
        this.usersMapper = usersMapper;

        // 创建专门用于字符串模板解析的 TemplateEngine
        StringTemplateResolver stringResolver = new StringTemplateResolver();
        stringResolver.setTemplateMode(TemplateMode.HTML);
        stringResolver.setCacheable(false);
        this.stringTemplateEngine = new TemplateEngine();
        this.stringTemplateEngine.setTemplateResolver(stringResolver);
        // 复用原引擎的 dialect（包含 th:text 等标准表达式）
        this.stringTemplateEngine.setDialects(templateEngine.getDialects());
    }

    @Override
    public void sendHtmlEmail(String to, String htmlContent, String from) {
        EmailTemplates emailTemplate = getTemplateFromDb("private_reply_notification");
        if (emailTemplate == null) {
            log.warn("数据库中未找到模板 private_reply_notification，跳过邮件发送");
            return;
        }

        Context context = new Context();
        context.setVariable("from", from);
        context.setVariable("message", htmlContent);

        String processedHtml = stringTemplateEngine.process(emailTemplate.getHtmlContent(), context);
        sendMimeEmail(to, emailTemplate.getEmailSubject(), processedHtml);
    }

    @Override
    public void sendVerificationEmail(String to, String code) {
        EmailTemplates emailTemplate = getTemplateFromDb("verification_email");
        if (emailTemplate == null) {
            log.warn("数据库中未找到模板 verification_email，跳过邮件发送");
            return;
        }

        Context context = new Context();
        context.setVariable("verificationCode", code);

        String processedHtml = stringTemplateEngine.process(emailTemplate.getHtmlContent(), context);
        sendMimeEmail(to, emailTemplate.getEmailSubject(), processedHtml);
    }

    @Async
    @Override
    public void sendPrivateReplyEmail(Long replyId) {
        PrivateReplies reply = privateRepliesMapper.selectById(replyId);
        if (reply == null) {
            log.warn("私信回复 {} 不存在，跳过邮件发送", replyId);
            return;
        }

        Users sender = usersMapper.selectById(reply.getSenderId());
        if (sender == null) {
            log.warn("发送者 {} 不存在，跳过邮件发送", reply.getSenderId());
            return;
        }

        Users receiver = usersMapper.selectById(reply.getReceiverId());
        if (receiver == null) {
            log.warn("接收者 {} 不存在，跳过邮件发送", reply.getReceiverId());
            return;
        }

        EmailTemplates emailTemplate = getTemplateFromDb("private_reply_notification");
        if (emailTemplate == null) {
            log.warn("数据库中未找到模板 private_reply_notification，跳过邮件发送");
            return;
        }

        Context context = new Context();
        context.setVariable("from", sender.getUsername());
        context.setVariable("message", reply.getReplyBody());

        String processedHtml = stringTemplateEngine.process(emailTemplate.getHtmlContent(), context);
        sendMimeEmail(receiver.getEmail(), emailTemplate.getEmailSubject(), processedHtml);

        reply.setSentAt(new Date());
        privateRepliesMapper.updateById(reply);
        log.info("私信回复 {} 邮件已发送至 {}", replyId, receiver.getEmail());
    }

    private EmailTemplates getTemplateFromDb(String templateName) {
        QueryWrapper<EmailTemplates> wrapper = new QueryWrapper<>();
        wrapper.eq("template_name", templateName);
        return emailTemplatesMapper.selectOne(wrapper);
    }

    private void sendMimeEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送邮件失败: " + e.getMessage(), e);
        }
    }
}
