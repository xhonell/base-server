package com.xhonell.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xhonell.common.enums.common.SystemErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName Result
 * description:
 * author: xhonell
 * create: 2025年10月19日01时01分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result(SystemErrorEnum errorEnum, T data) {
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
        this.data = data;
    }

    public Result(SystemErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> success() {
        return new Result<>(SystemErrorEnum.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SystemErrorEnum.SUCCESS, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SystemErrorEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(SystemErrorEnum.SYSTEM_ERROR.getCode(), message);
    }

    public static <T> Result<T> error(SystemErrorEnum errorEnum) {
        return new Result<>(errorEnum);
    }

    public static <T> Result<T> error() {
        return error(SystemErrorEnum.SYSTEM_ERROR);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
}
