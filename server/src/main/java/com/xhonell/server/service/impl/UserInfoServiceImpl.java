package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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

    @Override
    public Long getUserPoints(Long userId) {
        UserInfo userInfo = getByUserId(userId);
        if (userInfo == null) {
            return 0L;
        }
        return userInfo.getIntegral() == null ? 0L : userInfo.getIntegral();
    }

    @Override
    public Long addPoints(Long userId, Integer points) {
        // 查询用户信息
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        UserInfo userInfo = getOne(queryWrapper);

        if (userInfo == null) {
            throw new RuntimeException("用户信息不存在");
        }

        // 计算增加后的积分
        Long currentPoints = userInfo.getIntegral() == null ? 0L : userInfo.getIntegral();
        Long afterPoints = currentPoints + points;

        // 积分不能为负数
        if (afterPoints < 0) {
            throw new RuntimeException("积分不足");
        }

        // 更新用户积分
        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInfo::getUserId, userId);
        updateWrapper.set(UserInfo::getIntegral, afterPoints);
        update(updateWrapper);

        return afterPoints;
    }
}
