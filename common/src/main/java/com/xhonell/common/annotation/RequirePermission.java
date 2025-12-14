package com.xhonell.common.annotation;

import java.lang.annotation.*;

/**
 * program: BaseServer
 * ClassName RequirePermission
 * description: 权限校验注解，用于标记需要特定权限的接口
 * author: xhonell
 * create: 2025年11月03日
 * Version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 需要的权限标识（permission code）
     * 支持多个权限，满足其中一个即可访问
     */
    String[] value() default {};

    /**
     * 逻辑关系：AND - 需要同时拥有所有权限，OR - 拥有任意一个权限即可
     * 默认为 OR
     */
    Logical logical() default Logical.OR;

    enum Logical {
        /**
         * 逻辑或：拥有任意一个权限即可
         */
        OR,
        /**
         * 逻辑与：需要同时拥有所有权限
         */
        AND
    }
}
