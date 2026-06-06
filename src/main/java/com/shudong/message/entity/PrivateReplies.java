package com.shudong.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 私信回复表
 * @TableName private_replies
 */
@TableName(value ="private_replies")
@Data
public class PrivateReplies {
    /**
     * 私信回复ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 发送者用户ID
     */
    private Long senderId;

    /**
     * 接收者用户ID
     */
    private Long receiverId;

    /**
     * 内容
     */
    private String replyBody;

    /**
     * 状态：PENDING=待审核，APPROVED=已通过，REJECTED=已拒绝
     */
    private String replyStatus;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 审核时间
     */
    private Date reviewedAt;

    /**
     * 发送时间
     */
    private Date sentAt;

    /**
     * 邮件内容
     */
    private String emailContent;

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
        PrivateReplies other = (PrivateReplies) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
            && (this.getReceiverId() == null ? other.getReceiverId() == null : this.getReceiverId().equals(other.getReceiverId()))
            && (this.getReplyBody() == null ? other.getReplyBody() == null : this.getReplyBody().equals(other.getReplyBody()))
            && (this.getReplyStatus() == null ? other.getReplyStatus() == null : this.getReplyStatus().equals(other.getReplyStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getReviewedAt() == null ? other.getReviewedAt() == null : this.getReviewedAt().equals(other.getReviewedAt()))
            && (this.getSentAt() == null ? other.getSentAt() == null : this.getSentAt().equals(other.getSentAt()))
            && (this.getEmailContent() == null ? other.getEmailContent() == null : this.getEmailContent().equals(other.getEmailContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getReceiverId() == null) ? 0 : getReceiverId().hashCode());
        result = prime * result + ((getReplyBody() == null) ? 0 : getReplyBody().hashCode());
        result = prime * result + ((getReplyStatus() == null) ? 0 : getReplyStatus().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getReviewedAt() == null) ? 0 : getReviewedAt().hashCode());
        result = prime * result + ((getSentAt() == null) ? 0 : getSentAt().hashCode());
        result = prime * result + ((getEmailContent() == null) ? 0 : getEmailContent().hashCode());
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
        sb.append(", senderId=").append(senderId);
        sb.append(", receiverId=").append(receiverId);
        sb.append(", replyBody=").append(replyBody);
        sb.append(", replyStatus=").append(replyStatus);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", reviewedAt=").append(reviewedAt);
        sb.append(", sentAt=").append(sentAt);
        sb.append(", emailContent=").append(emailContent);
        sb.append("]");
        return sb.toString();
    }
}