package com.xhonell.common.aspect;

import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.annotation.RequireRole;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.exception.BizException;
import com.xhonell.common.utils.RedisUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * program: BaseServer
 * ClassName PermissionAspect
 * description: 权限校验切面
 * author: xhonell
 * create: 2025年11月03日
 * Version 1.0
 */
@Aspect
@Component
@Order(2)
@Slf4j
public class PermissionAspect {

    /**
     * 权限校验切面
     */
    @Around("@annotation(com.xhonell.common.annotation.RequirePermission) || @within(com.xhonell.common.annotation.RequirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 优先获取方法上的注解
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);

        // 如果方法上没有，获取类上的注解
        if (annotation == null) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(RequirePermission.class);
        }

        if (annotation == null) {
            return joinPoint.proceed();
        }

        String[] permissions = annotation.value();
        RequirePermission.Logical logical = annotation.logical();

        // 检查权限
        boolean hasPermission;
        if (logical == RequirePermission.Logical.AND) {
            hasPermission = RedisUserUtil.hasAllPermissions(permissions);
        } else {
            hasPermission = RedisUserUtil.hasAnyPermission(permissions);
        }

        if (!hasPermission) {
            log.warn("用户ID: {} 尝试访问 {}.{} 但权限不足，需要权限: {}, 逻辑: {}",
                    RedisUserUtil.getUserId(),
                    joinPoint.getTarget().getClass().getSimpleName(),
                    method.getName(),
                    String.join(", ", permissions),
                    logical);
            throw new BizException(SystemErrorEnum.USER_NOT_PERMISSION);
        }

        return joinPoint.proceed();
    }

    /**
     * 角色校验切面
     */
    @Around("@annotation(com.xhonell.common.annotation.RequireRole) || @within(com.xhonell.common.annotation.RequireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 优先获取方法上的注解
        RequireRole annotation = method.getAnnotation(RequireRole.class);

        // 如果方法上没有，获取类上的注解
        if (annotation == null) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(RequireRole.class);
        }

        if (annotation == null) {
            return joinPoint.proceed();
        }

        long[] roleIds = annotation.value();
        RequireRole.Logical logical = annotation.logical();

        // 将 long[] 转换为 Long[]
        Long[] roleIdObjects = new Long[roleIds.length];
        for (int i = 0; i < roleIds.length; i++) {
            roleIdObjects[i] = roleIds[i];
        }

        // 检查角色
        boolean hasRole;
        if (logical == RequireRole.Logical.AND) {
            hasRole = RedisUserUtil.hasAllRoles(roleIdObjects);
        } else {
            hasRole = RedisUserUtil.hasAnyRole(roleIdObjects);
        }

        if (!hasRole) {
            log.warn("用户ID: {} 尝试访问 {}.{} 但角色不足，需要角色ID: {}, 逻辑: {}",
                    RedisUserUtil.getUserId(),
                    joinPoint.getTarget().getClass().getSimpleName(),
                    method.getName(),
                    roleIds,
                    logical);
            throw new BizException(SystemErrorEnum.USER_NOT_PERMISSION);
        }

        return joinPoint.proceed();
    }
}
