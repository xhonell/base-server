package com.xhonell.common.advice;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

/**
 * program: BaseServer
 * ClassName GlobalExceptionHandler
 * description:
 * author: xhonell
 * create: 2025年10月19日01时23分
 * Version 1.0
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * ✅ 自定义业务异常
     */
    @ExceptionHandler(BizException.class)
    public Result<?> handleBusinessException(BizException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * ✅ 参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError() != null ?
                e.getBindingResult().getFieldError().getDefaultMessage() : "参数校验失败";
        log.warn("参数校验异常: {}", msg);
        return Result.error(SystemErrorEnum.SYSTEM_ERROR.getCode(), msg);
    }

    /**
     * ✅ 参数绑定异常（表单）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String msg = e.getFieldError() != null ? e.getFieldError().getDefaultMessage() : "参数绑定异常";
        return Result.error(SystemErrorEnum.SYSTEM_ERROR.getCode(), msg);
    }

    /**
     * ✅ 请求方法错误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法错误: {}", e.getMessage());
        return Result.error(SystemErrorEnum.SYSTEM_ERROR.getCode(), "请求方法不支持：" + e.getMethod());
    }

    /**
     * ✅ 请求体解析失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析错误: {}", e.getMessage());
        return Result.error(SystemErrorEnum.SYSTEM_ERROR.getCode(), "请求体格式错误");
    }

    /**
     * ✅ 文件上传大小限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        return Result.error(SystemErrorEnum.SYSTEM_ERROR.getCode(), "上传文件过大，请压缩后再试");
    }

    /**
     * ✅ IO类异常
     */
    @ExceptionHandler(IOException.class)
    public Result<?> handleIOException(IOException e) {
        log.error("IO异常: ", e);
        return Result.error(SystemErrorEnum.SYSTEM_ERROR.getCode(), "文件读写错误");
    }

    /**
     * ✅ 通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(SystemErrorEnum.SYSTEM_ERROR.getCode(), "服务器内部错误，请联系管理员");
    }
}