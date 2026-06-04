package com.chao.shudongbackend.exception;

import com.chao.shudongbackend.model.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 
 * <p>统一处理应用程序中抛出的各种异常，返回标准化的错误响应</p>
 * 
 * @author chao
 * @version 1.0
 * @since 2025-10-06
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     *
     * @param e 业务异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 - 请求路径: {}, 错误码: {}, 错误信息: {}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常 - MethodArgumentNotValidException
     * 
     * <p>处理@RequestBody参数校验失败的情况</p>
     *
     * @param e 参数校验异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, 
                                                               HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        log.warn("参数校验异常 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(400, "参数校验失败: " + errorMessage);
    }

    /**
     * 处理参数校验异常 - ConstraintViolationException
     * 
     * <p>处理@RequestParam、@PathVariable参数校验失败的情况</p>
     *
     * @param e 参数校验异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleConstraintViolationException(ConstraintViolationException e, 
                                                            HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        
        log.warn("参数校验异常 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(400, "参数校验失败: " + errorMessage);
    }

    /**
     * 处理参数绑定异常 - BindException
     * 
     * <p>处理表单参数绑定失败的情况</p>
     *
     * @param e 参数绑定异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleBindException(BindException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        log.warn("参数绑定异常 - 请求路径: {}, 错误信息: {}", request.getRequestURI(), errorMessage);
        return Result.error(400, "参数绑定失败: " + errorMessage);
    }

    /**
     * 处理数据访问异常
     * 
     * <p>处理数据库操作相关的异常</p>
     *
     * @param e 数据访问异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("数据访问异常 - 请求路径: {}, 异常信息: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.error(500, "数据库操作失败，请稍后重试");
    }

    /**
     * 处理SQL异常
     * 
     * <p>处理SQL执行相关的异常</p>
     *
     * @param e SQL异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("SQL异常 - 请求路径: {}, SQL状态: {}, 错误码: {}, 异常信息: {}", 
                request.getRequestURI(), e.getSQLState(), e.getErrorCode(), e.getMessage(), e);
        return Result.error(500, "数据库操作异常，请稍后重试");
    }

    /**
     * 处理认证异常
     * 
     * <p>处理用户认证失败的情况</p>
     *
     * @param e 认证异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Object> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("认证异常 - 请求路径: {}, 异常信息: {}", request.getRequestURI(), e.getMessage());
        return Result.unauthorized();
    }

    /**
     * 处理权限不足异常
     * 
     * <p>处理用户权限不足的情况</p>
     *
     * @param e 权限不足异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("权限不足异常 - 请求路径: {}, 异常信息: {}", request.getRequestURI(), e.getMessage());
        return Result.forbidden();
    }

    /**
     * 处理HTTP方法不支持异常
     * 
     * <p>处理请求方法不支持的情况</p>
     *
     * @param e HTTP方法不支持异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, 
                                                                      HttpServletRequest request) {
        log.warn("HTTP方法不支持 - 请求路径: {}, 支持的方法: {}, 请求方法: {}", 
                request.getRequestURI(), e.getSupportedMethods(), e.getMethod());
        return Result.error(405, "请求方法不支持");
    }

    /**
     * 处理请求参数缺失异常
     * 
     * <p>处理必需请求参数缺失的情况</p>
     *
     * @param e 请求参数缺失异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, 
                                                                       HttpServletRequest request) {
        log.warn("请求参数缺失 - 请求路径: {}, 参数名: {}, 参数类型: {}", 
                request.getRequestURI(), e.getParameterName(), e.getParameterType());
        return Result.error(400, "缺少必需参数: " + e.getParameterName());
    }

    /**
     * 处理参数类型不匹配异常
     * 
     * <p>处理请求参数类型转换失败的情况</p>
     *
     * @param e 参数类型不匹配异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, 
                                                                   HttpServletRequest request) {
        log.warn("参数类型不匹配 - 请求路径: {}, 参数名: {}, 参数值: {}, 期望类型: {}", 
                request.getRequestURI(), e.getName(), e.getValue(), 
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        return Result.error(400, "参数类型错误: " + e.getName());
    }

    /**
     * 处理HTTP消息不可读异常
     * 
     * <p>处理请求体解析失败的情况</p>
     *
     * @param e HTTP消息不可读异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, 
                                                               HttpServletRequest request) {
        log.warn("HTTP消息不可读 - 请求路径: {}, 异常信息: {}", request.getRequestURI(), e.getMessage());
        return Result.error(400, "请求体格式错误，请检查JSON格式");
    }

    /**
     * 处理404异常
     * 
     * <p>处理请求路径不存在的情况</p>
     *
     * @param e 404异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("请求路径不存在 - 请求路径: {}, 请求方法: {}", 
                request.getRequestURI(), e.getHttpMethod());
        return Result.notFound();
    }

    /**
     * 处理其他未捕获的异常
     * 
     * <p>作为最后的异常处理兜底</p>
     *
     * @param e 异常
     * @param request HTTP请求
     * @return 统一错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 - 请求路径: {}, 异常类型: {}, 异常信息: {}", 
                request.getRequestURI(), e.getClass().getSimpleName(), e.getMessage(), e);
        return Result.serverError();
    }
}
