package com.xhonell.server.service;

import com.xhonell.common.domain.request.LoginRequest;
import com.xhonell.common.domain.request.UserRegisterRequest;
import jakarta.validation.Valid;

/**
 * program: BaseServer
 * ClassName LoginService
 * description:
 * author: xhonell
 * create: 2025年10月17日23时51分
 * Version 1.0
 **/
public interface LoginService {
    void sendRegisterCode(String email);

    void register(@Valid UserRegisterRequest request);

    String login(@Valid LoginRequest request);
}
