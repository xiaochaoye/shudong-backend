package com.shudong.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 管理员日志表
 * @TableName admin_logs
 */
@TableName(value ="admin_logs")
@Data
public class AdminLogs {
    /**
     * 日志ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 管理员用户ID
     */
    private Long adminId;

    /**
     * 操作
     */
    private String actionType;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 详情
     */
    private String extraData;

    /**
     * IP地址
     */
    private String ipAddress;

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
        AdminLogs other = (AdminLogs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAdminId() == null ? other.getAdminId() == null : this.getAdminId().equals(other.getAdminId()))
            && (this.getActionType() == null ? other.getActionType() == null : this.getActionType().equals(other.getActionType()))
            && (this.getTargetType() == null ? other.getTargetType() == null : this.getTargetType().equals(other.getTargetType()))
            && (this.getTargetId() == null ? other.getTargetId() == null : this.getTargetId().equals(other.getTargetId()))
            && (this.getExtraData() == null ? other.getExtraData() == null : this.getExtraData().equals(other.getExtraData()))
            && (this.getIpAddress() == null ? other.getIpAddress() == null : this.getIpAddress().equals(other.getIpAddress()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        result = prime * result + ((getActionType() == null) ? 0 : getActionType().hashCode());
        result = prime * result + ((getTargetType() == null) ? 0 : getTargetType().hashCode());
        result = prime * result + ((getTargetId() == null) ? 0 : getTargetId().hashCode());
        result = prime * result + ((getExtraData() == null) ? 0 : getExtraData().hashCode());
        result = prime * result + ((getIpAddress() == null) ? 0 : getIpAddress().hashCode());
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
        sb.append(", adminId=").append(adminId);
        sb.append(", actionType=").append(actionType);
        sb.append(", targetType=").append(targetType);
        sb.append(", targetId=").append(targetId);
        sb.append(", extraData=").append(extraData);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append(", createdAt=").append(createdAt);
        sb.append("]");
        return sb.toString();
    }
}