package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.domain.entity.RolePermission;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.List;

/**
 * program: BaseServer
 * ClassName RolePermissionService
 * description:
 * author: xhonell
 * create: 2025年11月01日18时04分
 * Version 1.0
 **/
@Service
public interface RolePermissionService extends IService<RolePermission> {

    void removeByRoleId(Long id);

    List<RolePermission> selectByRoleIds(List<Long> roleIds);

    List<Permission> selectPermissionByRoleIds(List<Long> roleIds);

    List<Permission> selectPermissionByRoleId(Long roleId);
}
