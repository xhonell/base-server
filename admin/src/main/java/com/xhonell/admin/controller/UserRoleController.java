package com.xhonell.admin.controller;

import com.xhonell.admin.service.UserRoleService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.UserRoleAssignRequest;
import com.xhonell.common.domain.response.UserRoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName UserRoleController
 * description: 用户角色管理控制器
 * author: xhonell
 * create: 2025年11月01日18时14分
 * Version 1.0
 **/
@RestController
@RequestMapping("/user-role")
@RequiredArgsConstructor
public class UserRoleController {
    private final UserRoleService userRoleService;

    /**
     * 获取用户的角色列表（含详情）
     * @param userId 用户ID
     * @return 用户角色响应列表
     */
    @GetMapping("/list/{userId}")
    @RequirePermission("admin:user-role:list")
    public Result<List<UserRoleResponse>> list(@PathVariable Long userId) {
        return Result.success(userRoleService.getUserRolesWithDetail(userId));
    }

    /**
     * 为用户分配角色（会先删除用户原有的所有角色，再分配新角色）
     * @param request 用户角色分配请求
     * @return 操作结果
     */
    @PostMapping("/assign")
    @RequirePermission("admin:user-role:assign")
    public Result<String> assign(@RequestBody UserRoleAssignRequest request) {
        userRoleService.assignRoles(request);
        return Result.success("角色分配成功");
    }

    /**
     * 为用户添加单个角色（不删除原有角色）
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 操作结果
     */
    @PostMapping("/add/{userId}/{roleId}")
    @RequirePermission("admin:user-role:add")
    public Result<String> add(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.addUserRole(userId, roleId);
        return Result.success("角色添加成功");
    }

    /**
     * 删除用户的指定角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/remove/{userId}/{roleId}")
    @RequirePermission("admin:user-role:remove")
    public Result<String> remove(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.removeUserRole(userId, roleId);
        return Result.success("角色删除成功");
    }

    /**
     * 删除用户的所有角色
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/remove-all/{userId}")
    @RequirePermission("admin:user-role:remove-all")
    public Result<String> removeAll(@PathVariable Long userId) {
        userRoleService.removeRolesByUserId(userId);
        return Result.success("所有角色删除成功");
    }
}
