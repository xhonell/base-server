package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName DailyPointsResponse
 * description: 每日积分统计响应类
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyPointsResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文章学习积分
     */
    private Integer articlePoints;

    /**
     * 文章学习次数
     */
    private Integer articleCount;

    /**
     * 视频学习积分
     */
    private Integer videoPoints;

    /**
     * 视频学习次数
     */
    private Integer videoCount;

    /**
     * 评论积分
     */
    private Integer commentPoints;

    /**
     * 评论次数
     */
    private Integer commentCount;

    /**
     * 点赞积分
     */
    private Integer likePoints;

    /**
     * 点赞次数
     */
    private Integer likeCount;

    /**
     * 收藏积分
     */
    private Integer collectPoints;

    /**
     * 收藏次数
     */
    private Integer collectCount;

    /**
     * 今日总积分
     */
    private Integer totalPoints;
}