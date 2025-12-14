package com.xhonell.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * program: BaseServer
 * ClassName MybatisPlusConfig
 * description:
 * author: xhonell
 * create: 2025年10月15日22时42分
 * Version 1.0
 **/
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件配置
     * @return MybatisPlusInterceptor 对象
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 配置数据库类型，这里以 MySQL 为例
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}