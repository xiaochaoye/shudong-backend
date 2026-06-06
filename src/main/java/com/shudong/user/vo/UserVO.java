package com.shudong.user.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.shudong.common.View;
import lombok.Data;

import java.util.Date;

/**
 * 用户视图对象
 * 用于控制返回给前端的用户信息字段，隐藏敏感数据
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    @JsonView(View.Basic.class)
    private Long id;

    /**
     * 邮箱
     */
    @JsonView(View.Basic.class)
    private String email;

    /**
     * 用户名
     */
    @JsonView(View.Basic.class)
    private String username;

    /**
     * 用户头像URL
     */
    @JsonView(View.Basic.class)
    private String avatar;

    /**
     * 匿名昵称
     */
    @JsonView(View.Basic.class)
    private String anonymousName;

    /**
     * 匿名头像URL
     */
    @JsonView(View.Basic.class)
    private String anonymousAvatar;

    /**
     * 是否管理员（仅Detail视图返回）
     */
    @JsonView(View.Detail.class)
    private Integer isAdmin;

    /**
     * 账号状态（仅Detail视图返回）
     */
    @JsonView(View.Detail.class)
    private String recordStatus;

    /**
     * 注册时间
     */
    @JsonView(View.Detail.class)
    private Date createdAt;

    /**
     * 最后登录时间
     */
    @JsonView(View.Detail.class)
    private Date lastLoginAt;
}
