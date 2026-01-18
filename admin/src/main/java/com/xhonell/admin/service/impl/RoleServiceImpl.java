package com.xhonell.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.RoleMapper;
import com.xhonell.admin.service.PermissionService;
import com.xhonell.admin.service.RolePermissionService;
import com.xhonell.admin.service.RoleService;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.domain.entity.Role;
import com.xhonell.common.domain.entity.RolePermission;
import com.xhonell.common.domain.request.RolePageRequest;
import com.xhonell.common.domain.request.RoleSaveRequest;
import com.xhonell.common.domain.response.PermissionTreeResponse;
import com.xhonell.common.domain.response.RoleDetailResponse;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.ListUtil;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.common.utils.TreeBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * program: BaseServer
 * ClassName RoleServiceImpl
 * description:
 * author: xhonell
 * create: 2025年11月01日18时06分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RolePermissionService rolePermissionService;

    @Override
    public PageInfo<Role> selectList(RolePageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<Role> roles = selectListBy(request);
        return PageUtils.toPageInfo(roles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20)
    public void saveBy(RoleSaveRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        save(role);

        List<RolePermission> rolePermissions = new ArrayList<>();
        request.getPermissionIds().forEach(permissionId -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        });
        rolePermissionService.saveBatch(rolePermissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20)
    public void updateBy(RoleSaveRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        updateById(role);

        List<RolePermission> rolePermissions = new ArrayList<>();
        request.getPermissionIds().forEach(permissionId -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        });
        rolePermissionService.removeByRoleId(role.getId());
        rolePermissionService.saveBatch(rolePermissions);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, id);
        updateWrapper.set(Role::getStatus, status);
        baseMapper.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20)
    public void deleteById(Long id) {
        removeById(id);
        rolePermissionService.removeByRoleId(id);
    }

    @Override
    public RoleDetailResponse detail(Long id) {
        Role role = getById(id);
        AssertUtil.isTrue(Objects.nonNull(role), "角色不存在");
        List<Permission> rolePermissions = rolePermissionService.selectPermissionByRoleId(id);
        List<PermissionTreeResponse> permissionTreeResponseList = ListUtil.toList(rolePermissions, PermissionTreeResponse.class);
        RoleDetailResponse roleDetailResponse = new RoleDetailResponse();
        BeanUtils.copyProperties(role, roleDetailResponse);
        List<PermissionTreeResponse> permissionTreeResponses = TreeBuilderUtil.buildTree(
                permissionTreeResponseList,
                PermissionTreeResponse::getId,
                PermissionTreeResponse::getParentId,
                PermissionTreeResponse::setChildrenPermission
        );
        roleDetailResponse.setPermissions(permissionTreeResponses);
        return roleDetailResponse;
    }

    @Override
    public List<Role> selectByStatus(List<Long> roleIds, Boolean status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId, roleIds);
        queryWrapper.eq(Role::getStatus, status);
        return baseMapper.selectList(queryWrapper);
    }

    private List<Role> selectListBy(RolePageRequest request) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Role::getId);
        return baseMapper.selectList(queryWrapper);
    }
}
