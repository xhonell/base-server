
package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.UserService;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.UserPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param request
     * @return
     */
    @GetMapping("/list")
    public Result<PageInfo<RedisUser>> list(UserPageRequest request) {
        return Result.success(userService.selectList(request));
    }
}
