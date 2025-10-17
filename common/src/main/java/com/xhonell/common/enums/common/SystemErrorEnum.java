package com.xhonell.common.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * program: BaseServer
 * ClassName SystemErrorEnum
 * description:
 * author: xhonell
 * create: 2025年10月18日00时08分
 * Version 1.0
 **/
@Getter
@AllArgsConstructor
public enum SystemErrorEnum {

    SYSTEM_ERROR(500, "系统异常");

    private final Integer code;
    private final String message;
}
