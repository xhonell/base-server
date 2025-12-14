package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.RoleService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Role;
import com.xhonell.common.domain.request.RolePageRequest;
import com.xhonell.common.domain.request.RoleSaveRequest;
import com.xhonell.common.domain.response.RoleDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * program: BaseServer
 * ClassName RoleController
 * description:
 * author: xhonell
 * create: 2025年11月01日18时12分
 * Version 1.0
 **/
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {


    private final RoleService roleService;

    @GetMapping("/list")
    @RequirePermission("admin:role:list")
    public Result<PageInfo<Role>> list(RolePageRequest request) {
        return Result.success( roleService.selectList(request));
    }

    @PostMapping("/save")
    @RequirePermission("admin:role:save")
    public Result<String> save(@RequestBody RoleSaveRequest request) {
        roleService.saveBy(request);
        return Result.success();
    }

    @PostMapping("/update")
    @RequirePermission("admin:role:update")
    public Result<String> update(@RequestBody RoleSaveRequest request) {
        roleService.updateBy(request);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    @RequirePermission("admin:role:status")
    public Result<String> status(@PathVariable Long id, @RequestBody Map<String, Object> body )  {
        roleService.updateStatus(id, (Boolean) body.get("status"));
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission("admin:role:delete")
    public Result<String> delete(@PathVariable Long id) {
        roleService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequirePermission("admin:role:detail")
    public Result<RoleDetailResponse> detail(@PathVariable Long id) {
        return Result.success(roleService.detail(id));
    }
}
