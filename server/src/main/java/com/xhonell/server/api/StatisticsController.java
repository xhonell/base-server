package com.xhonell.server.api;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.response.StatisticsResponse;
import com.xhonell.server.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * program: BaseServer
 * ClassName StatisticsController
 * description: 统计信息Controller
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取系统统计信息
     * @return 统计信息
     */
    @GetMapping("/info")
    public Result<StatisticsResponse> getStatistics() {
        StatisticsResponse statistics = statisticsService.getStatistics();
        return Result.success(statistics);
    }
}