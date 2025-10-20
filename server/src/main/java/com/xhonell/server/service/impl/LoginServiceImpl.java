package com.xhonell.server.service.impl;

import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.User;
import com.xhonell.common.domain.request.LoginRequest;
import com.xhonell.common.domain.request.UserRegisterRequest;
import com.xhonell.common.enums.user.RoleEnum;
import com.xhonell.common.properties.RedisPrefixProperties;
import com.xhonell.common.utils.*;
import com.xhonell.server.service.LoginService;
import com.xhonell.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    private final EmailUtil emailUtil;

    private final UserService userService;

    private final RedisUtil redisUtil;
    private final FileServiceImpl fileServiceImpl;

    public void sendRegisterCode(String email) {
        AssertUtil.isTrue(RegexUtil.isEmail(email), "邮箱不可用");
        emailUtil.sendRegisterCode(email);
    }

    public void register(UserRegisterRequest request) {
        AssertUtil.isTrue(!request.isPasswordConfirmed(), "密码不一致");
        AssertUtil.isTrue(!emailUtil.verifyRegisterCode(request.getEmail(), request.getCode()), "验证码错误");
        User user = userService.getByEmail(request.getEmail(), RoleEnum.USER.getCode());
        AssertUtil.isTrue(Objects.isNull(user), "邮箱已存在");
        user = new User().setRole(RoleEnum.USER.getCode());
        BeanUtils.copyProperties(request, user);
        String salt = PasswordUtil.generateSalt();
        String encryptPassword = PasswordUtil.encrypt(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);
        userService.save(user);
    }

    public String login(LoginRequest request) {
        User user = userService.getByEmail(request.getEmail(), RoleEnum.USER.getCode());
        AssertUtil.isTrue(Objects.nonNull(user), "用户不存在");
        AssertUtil.isTrue(Objects.equals(user.getStatus(), Boolean.TRUE), "用户被禁用, 请联系管理员");
        AssertUtil.isTrue(PasswordUtil.verify(request.getPassword(), user.getSalt(), user.getPassword()), "密码错误");
        String token = RandomUtil.randomLetter(20);
        String loginKey = String.format(RedisPrefixProperties.LOGIN_INFO, token);
        File userFile = fileServiceImpl.getById(user.getAvatarId());
        RedisUser redisUser = new RedisUser();
        BeanUtils.copyProperties(user, redisUser);
        redisUser.setAvatarUrl(userFile.getFilePathUrl());
        redisUtil.set(loginKey, redisUser, RedisPrefixProperties.EXPIRE_TIME_DAY);
        return token;
    }
}
