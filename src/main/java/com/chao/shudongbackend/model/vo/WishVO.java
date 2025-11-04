package com.chao.shudongbackend.model.vo;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 愿望视图对象
 */
@Data
public class WishVO implements Serializable {

    /**
     * 愿望ID
     */
    @JsonView(View.Basic.class)
    private Long id;

    /**
     * 愿望标题
     */
    @JsonView(View.Basic.class)
    private String title;

    /**
     * 愿望内容
     */
    @JsonView(View.Basic.class)
    private String content;

    /**
     * 是否匿名发布
     */
    @JsonView(View.Basic.class)
    private Boolean isAnonymous;

    /**
     * 发布者用户ID
     */
    @JsonView(View.My.class)
    private Long userId;

    /**
     * 发布者用户名（匿名时为空）
     */
    @JsonView(View.Detail.class)
    private String username;

    /**
     * 发布者头像（匿名时为空）
     */
    @JsonView(View.Detail.class)
    private String avatar;

    /**
     * 愿望状态：PENDING=待实现，COMPLETED=已实现
     */
    @JsonView(View.Basic.class)
    private String wishStatus;

    /**
     * 愿望完成时间
     */
    @JsonView(View.Detail.class)
    private Date completedAt;

    /**
     * 完成愿望的用户ID
     */
    @JsonView(View.Detail.class)
    private Long completedBy;

    /**
     * 完成愿望的用户名
     */
    @JsonView(View.Detail.class)
    private String completedByUsername;

    /**
     * 点赞数
     */
    @JsonView(View.Basic.class)
    private Integer likeCount;

    /**
     * 当前用户是否已点赞
     */
    @JsonView(View.My.class)
    private Boolean isLiked;

    /**
     * 创建时间
     */
    @JsonView(View.Basic.class)
    private Date createdAt;

    /**
     * 最后修改时间
     */
    @JsonView(View.Basic.class)
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}
