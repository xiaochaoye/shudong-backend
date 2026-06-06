package com.shudong.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户设置表，存储用户个性化设置
 * @TableName user_settings
 */
@TableName(value ="user_settings")
@Data
public class UserSettings {
    /**
     * 设置ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，关联users表
     */
    private Long userId;

    /**
     * 是否开启邮件通知
     */
    private Integer emailNotifications;

    /**
     * 是否开启推送通知
     */
    private Integer pushNotifications;

    /**
     * 是否开启AI分析
     */
    private Integer aiAnalysisEnabled;

    /**
     * 每日拾取上限
     */
    private Integer dailyPickLimit;

    /**
     * 夜间拾取上限
     */
    private Integer nightPickLimit;

    /**
     * 最后更新时间
     */
    private Date updatedAt;

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
        UserSettings other = (UserSettings) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getEmailNotifications() == null ? other.getEmailNotifications() == null : this.getEmailNotifications().equals(other.getEmailNotifications()))
            && (this.getPushNotifications() == null ? other.getPushNotifications() == null : this.getPushNotifications().equals(other.getPushNotifications()))
            && (this.getAiAnalysisEnabled() == null ? other.getAiAnalysisEnabled() == null : this.getAiAnalysisEnabled().equals(other.getAiAnalysisEnabled()))
            && (this.getDailyPickLimit() == null ? other.getDailyPickLimit() == null : this.getDailyPickLimit().equals(other.getDailyPickLimit()))
            && (this.getNightPickLimit() == null ? other.getNightPickLimit() == null : this.getNightPickLimit().equals(other.getNightPickLimit()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getEmailNotifications() == null) ? 0 : getEmailNotifications().hashCode());
        result = prime * result + ((getPushNotifications() == null) ? 0 : getPushNotifications().hashCode());
        result = prime * result + ((getAiAnalysisEnabled() == null) ? 0 : getAiAnalysisEnabled().hashCode());
        result = prime * result + ((getDailyPickLimit() == null) ? 0 : getDailyPickLimit().hashCode());
        result = prime * result + ((getNightPickLimit() == null) ? 0 : getNightPickLimit().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
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
        sb.append(", emailNotifications=").append(emailNotifications);
        sb.append(", pushNotifications=").append(pushNotifications);
        sb.append(", aiAnalysisEnabled=").append(aiAnalysisEnabled);
        sb.append(", dailyPickLimit=").append(dailyPickLimit);
        sb.append(", nightPickLimit=").append(nightPickLimit);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}