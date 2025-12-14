package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.UserRoleMapper;
import com.xhonell.admin.service.UserRoleService;
import com.xhonell.common.domain.entity.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * program: BaseServer
 * ClassName UserRoleServiceImpl
 * description:
 * author: xhonell
 * create: 2025年11月01日18时06分
 * Version 1.0
 **/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Override
    public List<UserRole> selectByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        return baseMapper.selectList(queryWrapper);
    }
}
