package com.shudong.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 帖子表，支持快乐区、难过区、许愿池
 * @TableName posts
 */
@TableName(value ="posts")
@Data
public class Posts implements Serializable {
    /**
     * 帖子ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布者用户ID，外键关联users.id
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子正文内容
     */
    private String content;

    /**
     * 帖子类型：happy=快乐区，sad=难过区，wish=许愿池
     */
    private Object postType;

    /**
     * 是否匿名发布
     */
    private Integer isAnonymous;

    /**
     * 是否已发布（用于审核流程）
     */
    private Integer isPublished;

    /**
     * 浏览次数，用于热门排序
     */
    private Object viewCount;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 最后修改时间
     */
    private Date updatedAt;

    /**
     * 软删除时间，为空表示未删除
     */
    private Date deletedAt;

    /**
     * 愿望状态：PENDING=待实现，COMPLETED=已实现
     */
    private String wishStatus;

    /**
     * 愿望完成时间
     */
    private Date completedAt;

    /**
     * 完成愿望的用户ID，外键关联users.id
     */
    private Long completedBy;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 每日随机抽取次数（用于限制）
     */
    private Integer dailyRandomCount;

    /**
     * 最后随机抽取日期（用于重置计数）
     */
    private Date lastRandomDate;

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
        Posts other = (Posts) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getPostType() == null ? other.getPostType() == null : this.getPostType().equals(other.getPostType()))
            && (this.getIsAnonymous() == null ? other.getIsAnonymous() == null : this.getIsAnonymous().equals(other.getIsAnonymous()))
            && (this.getIsPublished() == null ? other.getIsPublished() == null : this.getIsPublished().equals(other.getIsPublished()))
            && (this.getViewCount() == null ? other.getViewCount() == null : this.getViewCount().equals(other.getViewCount()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getPostType() == null) ? 0 : getPostType().hashCode());
        result = prime * result + ((getIsAnonymous() == null) ? 0 : getIsAnonymous().hashCode());
        result = prime * result + ((getIsPublished() == null) ? 0 : getIsPublished().hashCode());
        result = prime * result + ((getViewCount() == null) ? 0 : getViewCount().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", postType=").append(postType);
        sb.append(", isAnonymous=").append(isAnonymous);
        sb.append(", isPublished=").append(isPublished);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
