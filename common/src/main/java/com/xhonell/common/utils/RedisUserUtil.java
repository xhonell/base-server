package com.xhonell.common.utils;

import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.enums.common.SystemErrorEnum;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * 检查当前用户是否拥有指定权限（OR逻辑）
     */
    public static boolean hasAnyPermission(String... permissions) {
        return checkPermissions(permissions, Stream::anyMatch);
    }

    /**
     * 检查当前用户是否拥有指定权限（AND逻辑）
     */
    public static boolean hasAllPermissions(String... permissions) {
        return checkPermissions(permissions, Stream::allMatch);
    }

    /**
     * 检查当前用户是否拥有指定角色（OR逻辑）
     */
    public static boolean hasAnyRole(Long... roleIds) {
        return checkRoles(roleIds, Stream::anyMatch);
    }

    /**
     * 检查当前用户是否拥有指定角色（AND逻辑）
     */
    public static boolean hasAllRoles(Long... roleIds) {
        return checkRoles(roleIds, Stream::allMatch);
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        return get().getId();
    }

    /**
     * 判断当前用户是否为超级管理员
     */
    public static boolean isSuperAdmin() {
        return Boolean.TRUE.equals(get().getSupperAdmin());
    }

    /**
     * 权限校验通用方法
     */
    private static boolean checkPermissions(String[] permissions, MatchStrategy matchStrategy) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        RedisUser user = get();
        if (isSuperAdmin()) {
            return true;
        }

        List<Permission> permissionList = user.getPermissionList();
        if (CollectionUtils.isEmpty(permissionList)) {
            return false;
        }

        Set<String> userPermissions = permissionList.stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        return matchStrategy.match(Arrays.stream(permissions), userPermissions::contains);
    }

    /**
     * 角色校验通用方法
     */
    private static boolean checkRoles(Long[] roleIds, MatchStrategy matchStrategy) {
        if (roleIds == null || roleIds.length == 0) {
            return true;
        }

        RedisUser user = get();
        if (isSuperAdmin()) {
            return true;
        }

        List<Long> userRoleIds = user.getRoleIds();
        if (CollectionUtils.isEmpty(userRoleIds)) {
            return false;
        }

        return matchStrategy.match(Arrays.stream(roleIds), userRoleIds::contains);
    }

    /**
     * 匹配策略函数式接口
     */
    @FunctionalInterface
    private interface MatchStrategy {
        <T> boolean match(Stream<T> stream, Predicate<T> predicate);
    }
}
