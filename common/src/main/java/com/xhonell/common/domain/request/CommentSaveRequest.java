package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * program: BaseServer
 * ClassName CommentSaveRequest
 * description: 评论保存请求类
 * author: xhonell
 * create: 2026/3/26
 * Version 1.0
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentSaveRequest {

    /**
     * 内容ID（文章/帖子/视频等）
     */
    @NotNull(message = "内容ID不能为空")
    private Long contentId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
}