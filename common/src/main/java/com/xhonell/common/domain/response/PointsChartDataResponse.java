package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * program: BaseServer
 * ClassName PointsChartDataResponse
 * description: 积分图表数据响应类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class    PointsChartDataResponse {

    /**
     * 总积分
     */
    private Long totalPoints;

    /**
     * 积分来源统计列表
     */
    private List<PointsSourceData> sources;

    /**
     * 积分来源数据
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PointsSourceData {

        /**
         * 来源名称
         */
        private String name;

        /**
         * 积分数量
         */
        private Long value;

        /**
         * 占比（百分比）
         */
        private Double percentage;
    }
}