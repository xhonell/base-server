package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName StatisticsResponse
 * description: 统计信息响应类
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticsResponse {

    /**
     * 文章数量
     */
    private Long articleCount;

    /**
     * 用户数量
     */
    private Long userCount;

    /**
     * 视频数量
     */
    private Long videoCount;

    /**
     * 内容总数（文章+视频）
     */
    private Long contentCount;
}