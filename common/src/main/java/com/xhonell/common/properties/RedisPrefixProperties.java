package com.xhonell.common.properties;

import lombok.Data;

/**
 * program: BaseServer
 * ClassName RedisPrefixPropertis
 * description:
 * author: xhonell
 * create: 2025年10月19日02时22分
 * Version 1.0
 **/
@Data
public class RedisPrefixProperties {

    /***
     * 一周过期时间
     */
    public static final Integer EXPIRE_TIME_WEEK = 60 * 60 * 24 * 7;

    /**
     * 短过期时间
     */
    public static final Integer EXPIRE_TIME_SHORT = 60 * 5;

    /**
     * 用户前缀
     */
    public static final String USER_TOKEN_PREFIX = "user:%s:";

    /**
     * 邮箱注册验证码前缀
     */
    public static final String EMAIL_REGISTER_CODE_PREFIX = "email:register:code:%s:";
}
