package com.xhonell.common.annotation;

import java.lang.annotation.*;

/**
 * program: BaseServer
 * ClassName RequireRole
 * description: 角色校验注解，用于标记需要特定角色的接口
 * author: xhonell
 * create: 2025年11月03日
 * Version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {

    /**
     * 需要的角色ID
     * 支持多个角色，满足其中一个即可访问
     */
    long[] value() default {};

    /**
     * 逻辑关系：AND - 需要同时拥有所有角色，OR - 拥有任意一个角色即可
     * 默认为 OR
     */
    Logical logical() default Logical.OR;

    enum Logical {
        /**
         * 逻辑或：拥有任意一个角色即可
         */
        OR,
        /**
         * 逻辑与：需要同时拥有所有角色
         */
        AND
    }
}
