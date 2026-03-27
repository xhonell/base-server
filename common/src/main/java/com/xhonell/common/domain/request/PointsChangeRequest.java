package com.xhonell.common.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * program: BaseServer
 * ClassName PointsChangeRequest
 * description: 积分变动请求类
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Data
public class PointsChangeRequest {

    /**
     * 来源类型（签到、下单、活动、管理员调整等）
     * 1 文章学习
     * 2 视频学习
     * 3 评论
     * 4 点赞
     * 5 收藏
     */
    @NotNull(message = "来源类型不能为空")
    private String sourceType;

    /**
     * 来源业务ID（订单ID/任务ID等）
     * 内容ID
     */
    private String sourceId;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 积分变动数量
     */
    private Integer points;
}