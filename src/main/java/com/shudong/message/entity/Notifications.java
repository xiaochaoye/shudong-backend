package com.shudong.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 站内通知表
 * @TableName notifications
 */
@TableName(value ="notifications")
@Data
public class Notifications {
    /**
     * 通知ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接收者用户ID
     */
    private Long userId;

    /**
     * 类型：PRIVATE_REPLY=私信回复，SYSTEM=系统通知
     */
    private String noticeType;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String noticeBody;

    /**
     * 是否已读
     */
    private Integer isRead;

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
        Notifications other = (Notifications) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNoticeType() == null ? other.getNoticeType() == null : this.getNoticeType().equals(other.getNoticeType()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getNoticeBody() == null ? other.getNoticeBody() == null : this.getNoticeBody().equals(other.getNoticeBody()))
            && (this.getIsRead() == null ? other.getIsRead() == null : this.getIsRead().equals(other.getIsRead()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNoticeType() == null) ? 0 : getNoticeType().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getNoticeBody() == null) ? 0 : getNoticeBody().hashCode());
        result = prime * result + ((getIsRead() == null) ? 0 : getIsRead().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", noticeType=").append(noticeType);
        sb.append(", title=").append(title);
        sb.append(", noticeBody=").append(noticeBody);
        sb.append(", isRead=").append(isRead);
        sb.append(", createdAt=").append(createdAt);
        sb.append("]");
        return sb.toString();
    }
}