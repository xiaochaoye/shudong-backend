package com.shudong.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 拾取记录表，存储用户拾取帖子的记录
 * @TableName pick_records
 */
@TableName(value = "pick_records")
@Data
public class PickRecord implements Serializable {
    /**
     * 拾取记录ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，关联users表
     */
    private Long userId;

    /**
     * 帖子ID，关联posts表
     */
    private Long postId;

    /**
     * 拾取类型：daily=每日精选，night=夜间精选，random=随机拾取
     */
    private String pickType;

    /**
     * 拾取时间
     */
    private Date pickedAt;

    /**
     * 是否已回应：0=未回应，1=已回应
     */
    private Integer isReplied;

    /**
     * 回应时间
     */
    private Date repliedAt;

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
        PickRecord other = (PickRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getPickType() == null ? other.getPickType() == null : this.getPickType().equals(other.getPickType()))
            && (this.getPickedAt() == null ? other.getPickedAt() == null : this.getPickedAt().equals(other.getPickedAt()))
            && (this.getIsReplied() == null ? other.getIsReplied() == null : this.getIsReplied().equals(other.getIsReplied()))
            && (this.getRepliedAt() == null ? other.getRepliedAt() == null : this.getRepliedAt().equals(other.getRepliedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getPickType() == null) ? 0 : getPickType().hashCode());
        result = prime * result + ((getPickedAt() == null) ? 0 : getPickedAt().hashCode());
        result = prime * result + ((getIsReplied() == null) ? 0 : getIsReplied().hashCode());
        result = prime * result + ((getRepliedAt() == null) ? 0 : getRepliedAt().hashCode());
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
        sb.append(", postId=").append(postId);
        sb.append(", pickType=").append(pickType);
        sb.append(", pickedAt=").append(pickedAt);
        sb.append(", isReplied=").append(isReplied);
        sb.append(", repliedAt=").append(repliedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
