package com.xhonell.server.service.impl;

import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.EmailUtil;
import com.xhonell.common.utils.RegexUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName LoginServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月17日23时51分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class LoginServiceImpl {

    private final EmailUtil emailUtil;

    public void sendRegisterCode(String email) {
        AssertUtil.isTrue(RegexUtil.isEmail(email), "邮箱不可用");
        emailUtil.sendRegisterCode(email);
    }
}
