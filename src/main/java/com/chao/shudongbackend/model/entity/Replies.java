package com.chao.shudongbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 难过区邮件回复记录表，支持管理员或系统发送
 * @TableName replies
 */
@TableName(value ="replies")
@Data
public class Replies implements Serializable {
    /**
     * 回复ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 被回复的难过区帖子ID
     */
    private Long postId;

    /**
     * 安慰邮件内容
     */
    private String replyContent;

    /**
     * 发送者用户ID，NULL表示系统自动发送
     */
    private Long senderUserId;

    /**
     * 发送者类型：admin=管理员，system=系统自动
     */
    private Object senderType;

    /**
     * 发送时间
     */
    private Date sentAt;

    /**
     * 发送状态：sent=成功，failed=失败
     */
    private Object status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        Replies other = (Replies) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getReplyContent() == null ? other.getReplyContent() == null : this.getReplyContent().equals(other.getReplyContent()))
            && (this.getSenderUserId() == null ? other.getSenderUserId() == null : this.getSenderUserId().equals(other.getSenderUserId()))
            && (this.getSenderType() == null ? other.getSenderType() == null : this.getSenderType().equals(other.getSenderType()))
            && (this.getSentAt() == null ? other.getSentAt() == null : this.getSentAt().equals(other.getSentAt()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getReplyContent() == null) ? 0 : getReplyContent().hashCode());
        result = prime * result + ((getSenderUserId() == null) ? 0 : getSenderUserId().hashCode());
        result = prime * result + ((getSenderType() == null) ? 0 : getSenderType().hashCode());
        result = prime * result + ((getSentAt() == null) ? 0 : getSentAt().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", postId=").append(postId);
        sb.append(", replyContent=").append(replyContent);
        sb.append(", senderUserId=").append(senderUserId);
        sb.append(", senderType=").append(senderType);
        sb.append(", sentAt=").append(sentAt);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}