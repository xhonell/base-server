package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName LearningHistoryRequest
 * description: 学习历史请求类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LearningHistoryRequest extends BasePageRequest {

    /**
     * 来源类型（1文章 2视频）
     */
    private Integer sourceType;
}