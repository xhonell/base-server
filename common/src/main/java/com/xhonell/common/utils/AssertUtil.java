package com.xhonell.common.utils;

import com.xhonell.common.exception.BizException;
import com.xhonell.common.enums.common.SystemErrorEnum;

/**
 * program: BaseServer
 * ClassName AssertUtil
 * description:
 * author: xhonell
 * create: 2025年10月19日00时50分
 * Version 1.0
 **/
public class AssertUtil {

    /**
     * 断言对象不成立 异常
     */
    public static void isTrue(Boolean flag, String message) {
        if (!flag) {
            throw new BizException(SystemErrorEnum.SYSTEM_ERROR.getCode(),  message);
        }
    }

    /**
     * 断言对象不成立 异常
     */
    public static void isTrue(Boolean flag, SystemErrorEnum errorEnum) {
        if (!flag) {
            throw new BizException(errorEnum);
        }
    }
}
