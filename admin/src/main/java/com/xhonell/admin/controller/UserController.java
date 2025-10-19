
package com.xhonell.admin.controller;

import com.xhonell.admin.service.UserService;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.dto.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/list")
    public Result<List<RedisUser>> list() {
        return Result.success(userService.selectList());
    }
}
