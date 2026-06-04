package com.shudong.model.enums;

/**
 * 愿望状态枚举
 */
public enum WishStatus {
    PENDING("待实现"),
    COMPLETED("已实现");

    private final String description;

    WishStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
