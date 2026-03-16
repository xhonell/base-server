package com.xhonell.server.service;

import com.xhonell.common.domain.response.StatisticsResponse;

/**
 * program: BaseServer
 * ClassName StatisticsService
 * description: 统计服务接口
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
public interface StatisticsService {

    /**
     * 获取系统统计信息
     * @return 统计信息响应
     */
    StatisticsResponse getStatistics();
}