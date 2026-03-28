package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName PointsDetailRequest
 * description: 积分明细请求类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsDetailRequest extends BasePageRequest {

    /**
     * 积分变动类型（0全部 1增加 2减少）
     */
    private Integer changeType;
}