package com.xhonell.common;

import com.xhonell.common.enums.common.SystemErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serial;

/**
 * program: BaseServer
 * ClassName BizException
 * description:
 * author: xhonell
 * create: 2025年10月18日00时06分
 * Version 1.0
 **/
@Getter
@Setter
public class BizException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer code;

    private final String message;

    /**
     * 默认构造函数
     */
    public BizException() {
        super(SystemErrorEnum.SYSTEM_ERROR.getMessage());
        this.code = SystemErrorEnum.SYSTEM_ERROR.getCode();
        this.message = SystemErrorEnum.SYSTEM_ERROR.getMessage();
    }

    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public BizException(String message) {
        super(message);
        this.code = SystemErrorEnum.SYSTEM_ERROR.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param systemErrorEnum 错误枚举
     */
    public BizException(SystemErrorEnum systemErrorEnum) {
        super(systemErrorEnum.getMessage());
        this.code = systemErrorEnum.getCode();
        this.message = systemErrorEnum.getMessage();
    }


}