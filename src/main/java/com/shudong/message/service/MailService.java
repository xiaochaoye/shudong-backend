package com.shudong.message.service;

public interface MailService {

    /**
     * 发送HTML格式邮件（主题和模板从数据库读取）
     * @param to            收件人邮箱
     * @param htmlContent   HTML内容
     * @param from          发件人名称
     */
    void sendHtmlEmail(String to, String htmlContent, String from);

    /**
     * 发送验证码邮件（主题和模板从数据库读取）
     * @param to            收件人邮箱
     * @param code          验证码
     */
    void sendVerificationEmail(String to, String code);

    /**
     * 发送私信回复邮件
     * @param replyId 私信回复ID
     */
    void sendPrivateReplyEmail(Long replyId);

}
