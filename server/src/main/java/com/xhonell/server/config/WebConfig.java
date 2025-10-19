package com.xhonell.server.config;

import com.xhonell.server.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * program: BaseServer
 * ClassName WebConfig
 * description:
 * author: xhonell
 * create: 2025年10月19日16时54分
 * Version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Autowired
    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")        // 拦截所有请求
                .excludePathPatterns(
                        "/swagger-ui/**",      // Swagger文档
                        "/v3/api-docs/**"
                );
    }
}