package com.shudong.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 评论表
 * @TableName comments
 */
@TableName(value ="comments")
@Data
public class Comments {
    /**
     * 评论ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 被评论的帖子ID
     */
    private Long postId;

    /**
     * 评论者用户ID
     */
    private Long userId;

    /**
     * 父评论ID，用于回复评论
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String commentBody;

    /**
     * 状态：ACTIVE=活跃，DELETED=已删除
     */
    private String commentStatus;

    /**
     * 评论时间
     */
    private Date createdAt;

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
        Comments other = (Comments) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getCommentBody() == null ? other.getCommentBody() == null : this.getCommentBody().equals(other.getCommentBody()))
            && (this.getCommentStatus() == null ? other.getCommentStatus() == null : this.getCommentStatus().equals(other.getCommentStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getCommentBody() == null) ? 0 : getCommentBody().hashCode());
        result = prime * result + ((getCommentStatus() == null) ? 0 : getCommentStatus().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
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
        sb.append(", postId=").append(postId);
        sb.append(", userId=").append(userId);
        sb.append(", parentId=").append(parentId);
        sb.append(", commentBody=").append(commentBody);
        sb.append(", commentStatus=").append(commentStatus);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append("]");
        return sb.toString();
    }
}