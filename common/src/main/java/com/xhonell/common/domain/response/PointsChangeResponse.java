package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName PointsChangeResponse
 * description: 积分变动响应类
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsChangeResponse {

    /**
     * 变动前积分
     */
    private Integer beforePoints;

    /**
     * 变动数量
     */
    private Integer changePoints;

    /**
     * 变动后积分
     */
    private Integer afterPoints;
}