package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * program: BaseServer
 * ClassName TopicCommentRequest
 * description: 话题评论请求类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicCommentRequest {

    /**
     * 话题ID
     */
    @NotNull(message = "话题ID不能为空")
    private Long topicId;

    /**
     * 父评论ID（用于回复，如果是直接评论则为null）
     */
    private Long parentId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
}