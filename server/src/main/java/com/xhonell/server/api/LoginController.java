package com.xhonell.server.api;

import com.xhonell.common.annotation.NoAuth;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.LoginRequest;
import com.xhonell.common.domain.request.UserRegisterRequest;
import com.xhonell.common.utils.EmailUtil;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.server.service.LoginService;
import com.xhonell.server.service.impl.LoginServiceImpl;
import jakarta.validation.Valid;
import lombok.Getter;
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

    /**
     * 发送注册验证码
     *
     * @param email 收件人邮箱
     * @return 提示信息
     */
    @PostMapping("/sendRegisterCode")
    @NoAuth
    public Result<Void> sendRegisterCode(@RequestParam String email) {
        loginService.sendRegisterCode(email);
        return Result.success();
    }

    /**
     * 注册
     *
     * @param request 注册信息
     * @return 提示信息
     */
    @PostMapping("/register")
    @NoAuth
    public Result<Void> register(@Valid @RequestBody UserRegisterRequest request) {
        loginService.register(request);
        return Result.success();
    }

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