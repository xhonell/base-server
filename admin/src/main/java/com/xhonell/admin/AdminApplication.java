package com.xhonell.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * program: BaseServer
 * ClassName AdminApplication
 * description:
 * author: xhonell
 * create: 2025年10月19日22时45分
 * Version 1.0
 **/
@SpringBootApplication(scanBasePackages = {"com.xhonell.common", "com.xhonell.admin"})
@EnableDiscoveryClient
@MapperScan("com.xhonell.admin.mapper")
public class AdminApplication {

    /**
     * springboot主启动类
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}

