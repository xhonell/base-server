package com.xhonell.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.domain.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * program: BaseServer
 * ClassName RolePermissionMapper
 * description:
 * author: xhonell
 * create: 2025年11月01日15时01分
 * Version 1.0
 **/
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Permission> selectPermissionByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<Permission> selectPermissionByRoleId(Long roleId);
}
