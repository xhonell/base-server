
package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.UserService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.AdminCreateRequest;
import com.xhonell.common.domain.request.UserPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName BannerController
 * description:
 * author: xhonell
 * create: 2025年10月19日22时51分
 * Version 1.0
 **/
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 获取用户列表
     * @param request 请求体
     * @return 响应体
     */
    @GetMapping("/list")
    @RequirePermission("admin:user:list")
    public Result<PageInfo<RedisUser>> list(UserPageRequest request) {
        return Result.success(userService.selectList(request));
    }

    /**
     * 新增管理员
     * @param request 新增管理员请求
     * @return 响应体
     */
    @PostMapping("/create")
    @RequirePermission("admin:user:create")
    public Result<Void> create(@RequestBody AdminCreateRequest request) {
        userService.createAdmin(request);
        return Result.success();
    }
}
