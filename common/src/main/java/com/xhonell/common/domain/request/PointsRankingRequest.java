package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName PointsRankingRequest
 * description: 积分排行榜请求类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsRankingRequest extends BasePageRequest {
}