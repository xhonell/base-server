package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * program: BaseServer
 * ClassName TopicSaveRequest
 * description: 话题保存请求类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicSaveRequest {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 分类（1谈论 2问答 3分享 4活动）
     */
    @NotNull(message = "分类不能为空")
    private Integer category;
}