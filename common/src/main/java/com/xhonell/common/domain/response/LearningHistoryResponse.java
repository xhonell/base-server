package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName LearningHistoryResponse
 * description: 学习历史响应类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LearningHistoryResponse {

    /**
     * 积分记录ID
     */
    private Long id;

    /**
     * 来源类型（1文章 2视频）
     */
    private Integer sourceType;

    /**
     * 来源类型名称
     */
    private String sourceTypeName;

    /**
     * 内容ID（文章ID或视频ID）
     */
    private String sourceId;

    /**
     * 获得积分
     */
    private Integer points;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}