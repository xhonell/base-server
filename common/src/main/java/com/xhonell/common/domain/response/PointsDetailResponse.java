package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName PointsDetailResponse
 * description: 积分明细响应类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsDetailResponse {

    /**
     * 积分记录ID
     */
    private Long id;

    /**
     * 积分变动数量（正数增加，负数减少）
     */
    private Integer changePoints;

    /**
     * 变动前积分
     */
    private Integer beforePoints;

    /**
     * 变动后积分
     */
    private Integer afterPoints;

    /**
     * 来源类型（1文章学习 2视频学习 3评论 4点赞 5收藏）
     */
    private String sourceType;

    /**
     * 来源类型名称
     */
    private String sourceTypeName;

    /**
     * 来源业务ID（订单ID/任务ID等）
     */
    private String sourceId;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}