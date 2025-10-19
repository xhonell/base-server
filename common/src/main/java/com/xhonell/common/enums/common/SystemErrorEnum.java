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


    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统异常"),
    USER_NOT_LOGIN(401, "用户未登录"),
    UPLOAD_FAILED(10001, "文件上传失败");

    private final Integer code;
    private final String message;
}
