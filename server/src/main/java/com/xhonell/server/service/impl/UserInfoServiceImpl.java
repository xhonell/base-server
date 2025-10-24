package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.UserInfo;
import com.xhonell.server.mapper.UserInfoMapper;
import com.xhonell.server.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * program: BaseServer
 * ClassName UserInfoServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月24日23时09分
 * Version 1.0
 **/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Override
    public UserInfo getByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return null;
        }
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }
}
