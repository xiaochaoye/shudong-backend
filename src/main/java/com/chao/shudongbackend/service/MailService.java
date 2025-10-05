package com.chao.shudongbackend.service;

public interface MailService {

    /**
     * 发送HTML格式邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param htmlContent HTML内容
     * @param from 发件人邮箱
     */
    void sendHtmlEmail(String to, String subject, String htmlContent, String from);
    
}
