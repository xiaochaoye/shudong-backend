package com.chao.shudongbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/*
 * 统一响应结果类
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    
    private int code;
    private String message;

    // 仅当 data 不为 null 时序列化
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @description 成功返回（带数据）
     * @param <T>
     * @param data
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(200, "操作成功", data);
    } 

    /**
     * @description 成功返回（无数据）
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return new Result<T>(200, "操作成功", null);
    }
    
    /**
     * @description 成功返回（自定义消息）
     * @param <T>
     * @param message
     * @return
     */
    public static <T> Result<T> success(String message) {
        return new Result<T>(200, message, null);
    }

    /**
     * @description 成功返回（消息 ＋ 数据）
     * @param <T>
     * @param message
     * @param data
     * @return
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(200, message, data);
    }

    /**
     * @description 失败返回
     * @param <T>
     * @param code
     * @param message
     * @return
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * @description 业务失败
     * @param <T>
     * @param message
     * @return
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * @description 未授权
     * @param <T>
     * @return
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未登录或登录已过期", null);
    }

    /**
     * @description 无权限
     * @param <T>
     * @return
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "权限不足", null);
    }

    /**
     * @description 资源未找到
     * @param <T>
     * @return
     */
    public static <T> Result<T> notFound() {
        return new Result<>(404, "资源未找到", null);
    }

    /**
     * @description 服务器错误
     * @param <T>
     * @return
     */
    public static <T> Result<T> serverError() {
        return new Result<>(500, "服务器内部错误", null);
    }

}
