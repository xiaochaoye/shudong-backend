package com.shudong.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 邮件模板表
 * @TableName email_templates
 */
@TableName(value ="email_templates")
@Data
public class EmailTemplates {
    /**
     * 模板ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 邮件主题
     */
    private String emailSubject;

    /**
     * HTML内容
     */
    private String htmlContent;

    /**
     * 创建时间
     */
    private Date createdAt;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EmailTemplates other = (EmailTemplates) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTemplateName() == null ? other.getTemplateName() == null : this.getTemplateName().equals(other.getTemplateName()))
            && (this.getEmailSubject() == null ? other.getEmailSubject() == null : this.getEmailSubject().equals(other.getEmailSubject()))
            && (this.getHtmlContent() == null ? other.getHtmlContent() == null : this.getHtmlContent().equals(other.getHtmlContent()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTemplateName() == null) ? 0 : getTemplateName().hashCode());
        result = prime * result + ((getEmailSubject() == null) ? 0 : getEmailSubject().hashCode());
        result = prime * result + ((getHtmlContent() == null) ? 0 : getHtmlContent().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", templateName=").append(templateName);
        sb.append(", emailSubject=").append(emailSubject);
        sb.append(", htmlContent=").append(htmlContent);
        sb.append(", createdAt=").append(createdAt);
        sb.append("]");
        return sb.toString();
    }
}