package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.UserMapper;
import com.xhonell.admin.service.UserService;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.User;
import com.xhonell.common.utils.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName UserServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月17日23时49分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByEmail(String email, Integer role) {
        AssertUtil.isTrue(Objects.nonNull(email), "邮箱不可为空");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        queryWrapper.eq(User::getRole, role);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<RedisUser> selectList() {
        return List.of();
    }
}
