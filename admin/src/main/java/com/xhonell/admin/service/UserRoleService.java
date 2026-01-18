package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.UserRole;
import com.xhonell.common.domain.request.UserRoleAssignRequest;
import com.xhonell.common.domain.response.UserRoleResponse;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID查询用户角色关联
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<UserRole> selectByUserId(Long userId);

    /**
     * 为用户分配角色（会先删除用户原有的所有角色，再分配新角色）
     * @param request 用户角色分配请求
     */
    void assignRoles(UserRoleAssignRequest request);

    /**
     * 为用户添加单个角色（不删除原有角色）
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void addUserRole(Long userId, Long roleId);

    /**
     * 删除用户的指定角色
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void removeUserRole(Long userId, Long roleId);

    /**
     * 删除用户的所有角色
     * @param userId 用户ID
     */
    void removeRolesByUserId(Long userId);

    /**
     * 获取用户的角色详情（包含角色名称、描述等）
     * @param userId 用户ID
     * @return 用户角色响应列表
     */
    List<UserRoleResponse> getUserRolesWithDetail(Long userId);
}
