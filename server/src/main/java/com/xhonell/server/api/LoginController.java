package com.xhonell.server.api;

import com.xhonell.common.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private final EmailUtil emailUtil;

    /**
     * 发送注册验证码
     * @param email 收件人邮箱
     * @return 提示信息
     */
    @PostMapping("/sendRegisterCode")
    public String sendRegisterCode(@RequestParam String email) {
        try {
            emailUtil.sendRegisterCode(email);
            return "验证码已发送到邮箱：" + email;
        } catch (Exception e) {
            return "发送失败：" + e.getMessage();
        }
    }
}