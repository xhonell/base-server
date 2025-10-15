package com.xhonell.gateway.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * program: BaseServer
 * ClassName TestController
 * description:
 * author: xhonell
 * create: 2025年08月17日23时53分
 * Version 1.0
 **/
@RestController
public class TestController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * 获取指定服务的实例
     *
     * @return
     */
    @GetMapping("/discovery")
    public List<String> services() {
        return discoveryClient.getInstances("SERVER-SERVICE")
                .stream()
                .map(si -> si.getUri().toString())
                .toList();
    }
}