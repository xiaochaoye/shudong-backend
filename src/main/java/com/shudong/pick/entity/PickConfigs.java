package com.shudong.pick.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 拾取配置表
 * @TableName pick_configs
 */
@TableName(value ="pick_configs")
@Data
public class PickConfigs {
    /**
     * 配置ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 每日上限
     */
    private Integer dailyLimit;

    /**
     * 夜间上限
     */
    private Integer nightLimit;

    /**
     * 冷却时间（小时）
     */
    private Integer cooldownHours;

    /**
     * 归档天数
     */
    private Integer archiveDays;

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
        PickConfigs other = (PickConfigs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDailyLimit() == null ? other.getDailyLimit() == null : this.getDailyLimit().equals(other.getDailyLimit()))
            && (this.getNightLimit() == null ? other.getNightLimit() == null : this.getNightLimit().equals(other.getNightLimit()))
            && (this.getCooldownHours() == null ? other.getCooldownHours() == null : this.getCooldownHours().equals(other.getCooldownHours()))
            && (this.getArchiveDays() == null ? other.getArchiveDays() == null : this.getArchiveDays().equals(other.getArchiveDays()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDailyLimit() == null) ? 0 : getDailyLimit().hashCode());
        result = prime * result + ((getNightLimit() == null) ? 0 : getNightLimit().hashCode());
        result = prime * result + ((getCooldownHours() == null) ? 0 : getCooldownHours().hashCode());
        result = prime * result + ((getArchiveDays() == null) ? 0 : getArchiveDays().hashCode());
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
        sb.append(", dailyLimit=").append(dailyLimit);
        sb.append(", nightLimit=").append(nightLimit);
        sb.append(", cooldownHours=").append(cooldownHours);
        sb.append(", archiveDays=").append(archiveDays);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}