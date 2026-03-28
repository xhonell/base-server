package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName PointsStatisticsResponse
 * description: 积分统计响应类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsStatisticsResponse {

    /**
     * 当前总积分
     */
    private Long totalPoints;

    /**
     * 本周获取积分
     */
    private Long thisWeekPoints;

    /**
     * 积分总排名
     */
    private Integer ranking;
}