package com.chao.shudongbackend.model.dto;

import lombok.Data;

/**
 * 愿望统计DTO
 */
@Data
public class WishStatsDTO {

    /**
     * 愿望总数
     */
    private Long totalCount;

    /**
     * 愿望已完成总数
     */
    private Long completedCount;

    /**
     * 愿望待实现总数
     */
    private Long pendingCount;
}
