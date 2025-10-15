package com.xhonell.server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * program: BaseServer
 * ClassName TestController
 * description:
 * author: xhonell
 * create: 2025年08月17日23时43分
 * Version 1.0
 **/
@RestController
@ResponseBody
@RequestMapping("/test")
public class TestController {

    /**
     * 测试接口
     * @return
     */
    @GetMapping("/test")
    public String test() {
        return "hello world!";
    }
}
