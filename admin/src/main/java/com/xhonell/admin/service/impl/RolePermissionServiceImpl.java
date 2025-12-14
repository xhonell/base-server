package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.RolePermissionMapper;
import com.xhonell.admin.service.RolePermissionService;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.domain.entity.RolePermission;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName RolePermissionService
 * description:
 * author: xhonell
 * create: 2025年11月01日18时06分
 * Version 1.0
 **/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
    @Override
    public void removeByRoleId(Long roleId) {
        LambdaUpdateWrapper<RolePermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RolePermission::getRoleId, roleId);
        baseMapper.delete(updateWrapper);
    }

    @Override
    public List<RolePermission> selectByRoleIds(List<Long> roleIds) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getRoleId, roleIds);
        List<RolePermission> rolePermissions = baseMapper.selectList(queryWrapper);
        return new ArrayList<>(
                rolePermissions.stream()
                        .collect(Collectors.toMap(
                                RolePermission::getPermissionId,
                                r -> r,
                                (r1, r2) -> r1
                        ))
                        .values()
        );
    }

    @Override
    public List<Permission> selectPermissionByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        roleIds = roleIds.stream().distinct().toList();
        List<Permission> permissions = baseMapper.selectPermissionByRoleIds(roleIds);
        return permissions.stream().distinct().toList();
    }

    @Override
    public List<Permission> selectPermissionByRoleId(Long roleId) {
        if (Objects.isNull(roleId)) {
            return new ArrayList<>();
        }
        return baseMapper.selectPermissionByRoleId(roleId);
    }
}
