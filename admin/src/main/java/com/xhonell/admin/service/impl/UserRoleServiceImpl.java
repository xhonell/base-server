package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.UserRoleMapper;
import com.xhonell.admin.service.RoleService;
import com.xhonell.admin.service.UserRoleService;
import com.xhonell.admin.service.UserService;
import com.xhonell.common.domain.entity.Role;
import com.xhonell.common.domain.entity.User;
import com.xhonell.common.domain.entity.UserRole;
import com.xhonell.common.domain.request.UserRoleAssignRequest;
import com.xhonell.common.domain.response.UserRoleResponse;
import com.xhonell.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName UserRoleServiceImpl
 * description:
 * author: xhonell
 * create: 2025年11月01日18时06分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final UserService userService;
    private final RoleService roleService;

    @Override
    public List<UserRole> selectByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20)
    public void assignRoles(UserRoleAssignRequest request) {
        Long userId = request.getUserId();
        List<Long> roleIds = request.getRoleIds();

        // 验证用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 删除用户原有的所有角色
        removeRolesByUserId(userId);

        // 如果角色列表为空，直接返回
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }

        // 验证角色是否存在且状态为启用
        List<Role> roles = roleService.selectByStatus(roleIds, true);
        if (roles.size() != roleIds.size()) {
            throw new BizException("部分角色不存在或已被禁用");
        }

        // 批量插入新的用户角色关联
        List<UserRole> userRoles = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(now);
            userRole.setUpdateTime(now);
            userRoles.add(userRole);
        }
        saveBatch(userRoles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRole(Long userId, Long roleId) {
        // 验证用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 验证角色是否存在且状态为启用
        Role role = roleService.getById(roleId);
        if (role == null) {
            throw new BizException("角色不存在");
        }
        if (!role.getStatus()) {
            throw new BizException("角色已被禁用");
        }

        // 检查用户是否已经拥有该角色
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId);
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BizException("用户已拥有该角色");
        }

        // 添加用户角色关联
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        LocalDateTime now = LocalDateTime.now();
        userRole.setCreateTime(now);
        userRole.setUpdateTime(now);
        save(userRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserRole(Long userId, Long roleId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId);
        baseMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRolesByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public List<UserRoleResponse> getUserRolesWithDetail(Long userId) {
        return baseMapper.getUserRolesWithDetail(userId);
    }
}
