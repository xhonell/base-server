package com.xhonell.common.utils;

import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.enums.common.SystemErrorEnum;

import java.util.Objects;

/**
 * program: BaseServer
 * ClassName RedisUserUtils
 * description:
 * author: xhonell
 * create: 2025年10月19日16时38分
 * Version 1.0
 **/
public class RedisUserUtil {
    private static final ThreadLocal<RedisUser> threadLocal = new InheritableThreadLocal<>();


    public static void set(RedisUser redisUser) {
        AssertUtil.isTrue(Objects.nonNull(redisUser), SystemErrorEnum.USER_NOT_LOGIN);
        threadLocal.set(redisUser);
    }

    public static RedisUser get() {
        RedisUser user = threadLocal.get();
        AssertUtil.isTrue(Objects.nonNull(user), SystemErrorEnum.USER_NOT_LOGIN);
        return user;
    }

    public static void remove() {
        threadLocal.remove();
    }
}
