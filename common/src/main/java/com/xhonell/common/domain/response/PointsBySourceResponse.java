package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName PointsBySourceResponse
 * description: 按来源统计积分响应类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsBySourceResponse {

    /**
     * 文章学习获得积分
     */
    private Long articlePoints;

    /**
     * 视频学习获得积分
     */
    private Long videoPoints;

    /**
     * 评论获得积分
     */
    private Long commentPoints;

    /**
     * 点赞获得积分
     */
    private Long likePoints;

    /**
     * 收藏获得积分
     */
    private Long collectPoints;

    /**
     * 其他来源获得积分
     */
    private Long otherPoints;

    /**
     * 总积分
     */
    private Long totalPoints;
}