package com.xhonell.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * program: BaseServer
 * ClassName ServerApplication
 * description:
 * author: xhonell
 * create: 2025年08月17日22时01分
 * Version 1.0
 **/
@SpringBootApplication(scanBasePackages = {"com.xhonell.gateway"})
@EnableDiscoveryClient
public class GateWayApplication {

    /**
     * springboot主启动类
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }
}
