package com.xhonell.admin.service.impl;

import com.xhonell.admin.service.*;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.*;
import com.xhonell.common.domain.request.LoginRequest;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.enums.user.RoleEnum;
import com.xhonell.common.properties.ConfigProperties;
import com.xhonell.common.properties.RedisPrefixProperties;
import com.xhonell.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName LoginServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月17日23时51分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {


    private final UserService userService;

    private final RedisUtil redisUtil;

    private final FileServiceImpl fileServiceImpl;

    private final RolePermissionService rolePermissionService;

    private final UserRoleService userRoleService;
    private final ConfigProperties configProperties;
    private final RoleService roleService;

    public String login(LoginRequest request) {
        User user = userService.getByEmail(request.getEmail(), RoleEnum.ADMIN.getCode());
        AssertUtil.isTrue(Objects.nonNull(user), "用户不存在");
        AssertUtil.isTrue(Objects.equals(user.getStatus(), Boolean.TRUE), "用户被禁用, 请联系管理员");
        AssertUtil.isTrue(PasswordUtil.verify(request.getPassword(), user.getSalt(), user.getPassword()), "密码错误");
        List<UserRole> userRoleList = userRoleService.selectByUserId(user.getId());
        AssertUtil.isTrue(!CollectionUtils.isEmpty(userRoleList), SystemErrorEnum.USER_NOT_PERMISSION);
        List<Long> roleIds = userRoleList.stream().map(UserRole::getRoleId).toList();
        List<Role> roleList = roleService.selectByStatus(roleIds, Boolean.TRUE);
        AssertUtil.isTrue(!CollectionUtils.isEmpty(roleList), "角色被禁用，联系管理员");
        List<Permission> rolePermissions = rolePermissionService.selectPermissionByRoleIds(roleIds);
        AssertUtil.isTrue(!CollectionUtils.isEmpty(rolePermissions), SystemErrorEnum.USER_NOT_PERMISSION);
        String token = RandomUtil.randomLetter(20);
        String loginKey = String.format(RedisPrefixProperties.LOGIN_INFO, token);
        File userFile = fileServiceImpl.getById(user.getAvatarId());
        RedisUser redisUser = new RedisUser();
        BeanUtils.copyProperties(user, redisUser);
        redisUser.setAvatarUrl(userFile.getFilePathUrl());
        redisUser.setPermissionList(rolePermissions);
        redisUser.setRoleIds(roleIds);
        redisUser.setSupperAdmin(configProperties.getSuperAdminIds().contains(user.getId()) ? Boolean.TRUE : Boolean.FALSE);
        redisUtil.set(loginKey, redisUser, RedisPrefixProperties.EXPIRE_TIME_DAY);
        return token;
    }
}
