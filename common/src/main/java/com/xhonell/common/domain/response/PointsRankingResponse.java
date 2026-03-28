package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName PointsRankingResponse
 * description: 积分排行榜响应类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsRankingResponse {

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像URL
     */
    private String avatarUrl;

    /**
     * 积分
     */
    private Long points;
}