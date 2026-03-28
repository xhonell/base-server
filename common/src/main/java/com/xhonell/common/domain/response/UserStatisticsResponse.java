package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName UserStatisticsResponse
 * description: 用户统计响应类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatisticsResponse {

    /**
     * 当前积分
     */
    private Long points;

    /**
     * 学习文章数
     */
    private Long articleCount;

    /**
     * 学习视频数
     */
    private Long videoCount;

    /**
     * 发表论贴数
     */
    private Long topicCount;
}