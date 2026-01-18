package com.xhonell.admin.controller;

import com.xhonell.admin.service.PermissionService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.domain.request.PermissionPageRequest;
import com.xhonell.common.domain.response.PermissionTreeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * program: BaseServer
 * ClassName PermissionController
 * description:
 * author: xhonell
 * create: 2025年11月01日18时11分
 * Version 1.0
 **/
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/list")
    @RequirePermission("admin:permission:list")
    public Result<List<Permission>> list(PermissionPageRequest request) {
        return Result.success( permissionService.selectList(request));
    }

    @PostMapping("/save")
    @RequirePermission("admin:permission:save")
    public Result<String> save(@RequestBody Permission permission) {
        permissionService.saveBy(permission);
        return Result.success();
    }

    @PostMapping("/update")
    @RequirePermission("admin:permission:update")
    public Result<String> update(@RequestBody Permission permission) {
        permissionService.updateBy(permission);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    @RequirePermission("admin:permission:status")
    public Result<String> status(@PathVariable Long id, @RequestBody Map<String, Object> body )  {
        permissionService.updateStatus(id, (Boolean) body.get("status"));
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission("admin:permission:delete")
    public Result<String> delete(@PathVariable Long id) {
        permissionService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/select")
    @RequirePermission("admin:permission:select")
    public Result<List<PermissionTreeResponse>> list() {
        return Result.success( permissionService.selectTreeByStatus());
    }

    /**
     * 获取当前登录用户的权限树
     * 用于前端菜单渲染和权限控制
     */
    @GetMapping("/current/tree")
    public Result<List<PermissionTreeResponse>> currentUserPermissionTree() {
        return Result.success(permissionService.selectCurrentUserPermissionTree());
    }
}
