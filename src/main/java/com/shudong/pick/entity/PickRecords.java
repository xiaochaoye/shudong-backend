package com.shudong.pick.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 拾取记录表
 * @TableName pick_records
 */
@TableName(value ="pick_records")
@Data
public class PickRecords {
    /**
     * 拾取记录ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 类型：CASUAL_VIEW= casual浏览，EXPRESS_RESONANCE=表达共鸣
     */
    private String pickType;

    /**
     * 拾取时间
     */
    private Date pickedAt;

    /**
     * 共鸣时间
     */
    private Date resonancedAt;

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
        PickRecords other = (PickRecords) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getPickType() == null ? other.getPickType() == null : this.getPickType().equals(other.getPickType()))
            && (this.getPickedAt() == null ? other.getPickedAt() == null : this.getPickedAt().equals(other.getPickedAt()))
            && (this.getResonancedAt() == null ? other.getResonancedAt() == null : this.getResonancedAt().equals(other.getResonancedAt()));
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
        result = prime * result + ((getResonancedAt() == null) ? 0 : getResonancedAt().hashCode());
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
        sb.append(", resonancedAt=").append(resonancedAt);
        sb.append("]");
        return sb.toString();
    }
}