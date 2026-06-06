package com.shudong.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户表，存储注册用户信息
 * @TableName users
 */
@TableName(value ="users")
@Data
public class Users {
    /**
     * 用户ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 邮箱，唯一，用于登录
     */
    private String email;

    /**
     * 用户名，可显示在帖子上
     */
    private String username;

    /**
     * 密码哈希值（使用 bcrypt 加密）
     */
    private String passwordHash;

    /**
     * 用户头像图片的URL链接
     */
    private String avatar;

    /**
     * 匿名昵称
     */
    private String anonymousName;

    /**
     * 匿名头像URL
     */
    private String anonymousAvatar;

    /**
     * 是否管理员
     */
    private Integer isAdmin;

    /**
     * 状态：ACTIVE=活跃，INACTIVE=非活跃，DELETED=已删除
     */
    private String recordStatus;

    /**
     * 注册时间
     */
    private Date createdAt;

    /**
     * 最后登录时间
     */
    private Date lastLoginAt;

    /**
     * 软删除时间
     */
    private Date deletedAt;

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
        Users other = (Users) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPasswordHash() == null ? other.getPasswordHash() == null : this.getPasswordHash().equals(other.getPasswordHash()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getAnonymousName() == null ? other.getAnonymousName() == null : this.getAnonymousName().equals(other.getAnonymousName()))
            && (this.getAnonymousAvatar() == null ? other.getAnonymousAvatar() == null : this.getAnonymousAvatar().equals(other.getAnonymousAvatar()))
            && (this.getIsAdmin() == null ? other.getIsAdmin() == null : this.getIsAdmin().equals(other.getIsAdmin()))
            && (this.getRecordStatus() == null ? other.getRecordStatus() == null : this.getRecordStatus().equals(other.getRecordStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getLastLoginAt() == null ? other.getLastLoginAt() == null : this.getLastLoginAt().equals(other.getLastLoginAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPasswordHash() == null) ? 0 : getPasswordHash().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getAnonymousName() == null) ? 0 : getAnonymousName().hashCode());
        result = prime * result + ((getAnonymousAvatar() == null) ? 0 : getAnonymousAvatar().hashCode());
        result = prime * result + ((getIsAdmin() == null) ? 0 : getIsAdmin().hashCode());
        result = prime * result + ((getRecordStatus() == null) ? 0 : getRecordStatus().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getLastLoginAt() == null) ? 0 : getLastLoginAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", email=").append(email);
        sb.append(", username=").append(username);
        sb.append(", passwordHash=").append(passwordHash);
        sb.append(", avatar=").append(avatar);
        sb.append(", anonymousName=").append(anonymousName);
        sb.append(", anonymousAvatar=").append(anonymousAvatar);
        sb.append(", isAdmin=").append(isAdmin);
        sb.append(", recordStatus=").append(recordStatus);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", lastLoginAt=").append(lastLoginAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append("]");
        return sb.toString();
    }
}