package com.xhonell.admin.controller;

import com.xhonell.admin.service.LoginService;
import com.xhonell.common.annotation.NoAuth;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.LoginRequest;
import com.xhonell.common.utils.RedisUserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName LoginController
 * description:
 * author: xhonell
 * create: 2025年10月18日00时18分
 * Version 1.0
 **/
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @PostMapping("/login")
    @NoAuth
    public Result<String> login(@Valid @RequestBody LoginRequest request) {
        String token = loginService.login(request);
        return Result.success(token);
    }

    @GetMapping("/info")
    public Result<RedisUser> info() {
        return Result.success(RedisUserUtil.get());
    }
}