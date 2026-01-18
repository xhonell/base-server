package com.xhonell.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhonell.common.domain.entity.UserRole;
import com.xhonell.common.domain.response.UserRoleResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName UserRoleMapper
 * description:
 * author: xhonell
 * create: 2025年11月01日14时54分
 * Version 1.0
 **/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 获取用户的角色详情（包含角色名称、描述等）
     * @param userId 用户ID
     * @return 用户角色响应列表
     */
    List<UserRoleResponse> getUserRolesWithDetail(Long userId);
}
