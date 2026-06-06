package com.shudong.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 帖子表
 * @TableName posts
 */
@TableName(value ="posts")
@Data
public class Posts {
    /**
     * 帖子ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布者用户ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子正文内容
     */
    private String postBody;

    /**
     * 状态：PUBLISHED=已发布，DELETED=已删除
     */
    private String postStatus;

    /**
     * 是否匿名发布
     */
    private Integer isAnonymous;

    /**
     * 是否私密
     */
    private Integer isPrivate;

    /**
     * 归档时间
     */
    private Date archivedAt;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 最后修改时间
     */
    private Date updatedAt;

    /**
     * 软删除时间
     */
    private Date deletedAt;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 共鸣次数
     */
    private Integer resonanceCount;

    /**
     * 评论次数
     */
    private Integer commentCount;

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
        Posts other = (Posts) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getPostBody() == null ? other.getPostBody() == null : this.getPostBody().equals(other.getPostBody()))
            && (this.getPostStatus() == null ? other.getPostStatus() == null : this.getPostStatus().equals(other.getPostStatus()))
            && (this.getIsAnonymous() == null ? other.getIsAnonymous() == null : this.getIsAnonymous().equals(other.getIsAnonymous()))
            && (this.getIsPrivate() == null ? other.getIsPrivate() == null : this.getIsPrivate().equals(other.getIsPrivate()))
            && (this.getArchivedAt() == null ? other.getArchivedAt() == null : this.getArchivedAt().equals(other.getArchivedAt()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getViewCount() == null ? other.getViewCount() == null : this.getViewCount().equals(other.getViewCount()))
            && (this.getResonanceCount() == null ? other.getResonanceCount() == null : this.getResonanceCount().equals(other.getResonanceCount()))
            && (this.getCommentCount() == null ? other.getCommentCount() == null : this.getCommentCount().equals(other.getCommentCount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getPostBody() == null) ? 0 : getPostBody().hashCode());
        result = prime * result + ((getPostStatus() == null) ? 0 : getPostStatus().hashCode());
        result = prime * result + ((getIsAnonymous() == null) ? 0 : getIsAnonymous().hashCode());
        result = prime * result + ((getIsPrivate() == null) ? 0 : getIsPrivate().hashCode());
        result = prime * result + ((getArchivedAt() == null) ? 0 : getArchivedAt().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getViewCount() == null) ? 0 : getViewCount().hashCode());
        result = prime * result + ((getResonanceCount() == null) ? 0 : getResonanceCount().hashCode());
        result = prime * result + ((getCommentCount() == null) ? 0 : getCommentCount().hashCode());
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
        sb.append(", title=").append(title);
        sb.append(", postBody=").append(postBody);
        sb.append(", postStatus=").append(postStatus);
        sb.append(", isAnonymous=").append(isAnonymous);
        sb.append(", isPrivate=").append(isPrivate);
        sb.append(", archivedAt=").append(archivedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", resonanceCount=").append(resonanceCount);
        sb.append(", commentCount=").append(commentCount);
        sb.append("]");
        return sb.toString();
    }
}