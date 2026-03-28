package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName ExamPaperPageRequest
 * description: 试卷分页查询请求
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamPaperPageRequest extends BasePageRequest {

    /**
     * 试卷名称（模糊查询）
     */
    private String title;

    /**
     * 试卷状态（0 未发布 1 已发布）
     */
    private Integer status;
}