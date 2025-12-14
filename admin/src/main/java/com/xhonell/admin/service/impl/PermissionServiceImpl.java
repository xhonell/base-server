package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.xhonell.admin.mapper.PermissionMapper;
import com.xhonell.admin.service.PermissionService;
import com.xhonell.admin.service.RolePermissionService;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.domain.entity.RolePermission;
import com.xhonell.common.domain.request.PermissionPageRequest;
import com.xhonell.common.domain.response.PermissionTreeResponse;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.properties.RedisPrefixProperties;
import com.xhonell.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * program: BaseServer
 * ClassName PermissionServiceImpl
 * description:
 * author: xhonell
 * create: 2025年11月01日18时06分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final RedisUtil redisUtil;
    private final RolePermissionService rolePermissionService;

    @Override
    public List<Permission> selectList(PermissionPageRequest request) {
        request.setParentId(Objects.nonNull(request.getParentId()) ? request.getParentId() : 0);
        return selectBy(request);
    }

    @Override
    public void saveBy(Permission permission) {
        save(permission);
        removeMenuCache();
    }

    @Override
    public void updateBy(Permission permission) {
        updateById(permission);
        removeMenuCache();
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Permission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Permission::getId, id);
        updateWrapper.set(Permission::getStatus, status);
        update(updateWrapper);
        removeMenuCache();
    }

    @Override
    public List<PermissionTreeResponse> selectTreeByStatus() {
        List<Permission> permissions = selectAllByStatus(Boolean.TRUE);
        List<PermissionTreeResponse> permissionTreeResponses = ListUtil.toList(permissions, PermissionTreeResponse.class);
        return TreeBuilderUtil.buildTree(
                permissionTreeResponses,
                PermissionTreeResponse::getId,
                PermissionTreeResponse::getParentId,
                PermissionTreeResponse::setChildrenPermission
        );
    }

    @Override
    public void deleteById(Long id) {
        removeById(id);
        removeMenuCache();
    }

    private List<Permission> selectBy(PermissionPageRequest request) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getParentId, request.getParentId());
        queryWrapper.orderByDesc(Permission::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    private List<Permission> selectAllByStatus(Boolean status) {
        RedisUser redisUser = RedisUserUtil.get();
        AssertUtil.isTrue(!CollectionUtils.isEmpty(redisUser.getRoleIds()), SystemErrorEnum.USER_NOT_PERMISSION);
        List<RolePermission> rolePermissions = rolePermissionService.selectByRoleIds(redisUser.getRoleIds());
        Map<Long, RolePermission> rolePermissionMap = ListUtil.toMap(rolePermissions, RolePermission::getPermissionId, Function.identity());
        String redisKey = String.format(RedisPrefixProperties.AUTH_PERMISSION_PREFIX, status);
        List<Permission> permissions = redisUtil.get(redisKey, new TypeReference<>() {
        });
        if (CollectionUtils.isEmpty(permissions)) {
            LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Permission::getStatus, status);
            queryWrapper.orderByDesc(Permission::getSort);
            permissions = baseMapper.selectList(queryWrapper);
            redisUtil.set(redisKey, permissions, RedisPrefixProperties.EXPIRE_TIME_DAY);
        }
        if (redisUser.getSupperAdmin()) {
            return permissions;
        }
        return permissions.stream()
                .filter(permission -> rolePermissionMap.containsKey(permission.getId()))
                .toList();
    }

    private void removeMenuCache (){
        String redisKey = String.format(RedisPrefixProperties.AUTH_PERMISSION_PREFIX, Boolean.TRUE);
        redisUtil.del(redisKey);
    }
}
