package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName CommentPageRequest
 * description: 评论分页请求类
 * author: xhonell
 * create: 2026/3/26
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentPageRequest extends BasePageRequest {

    /**
     * 内容ID（文章/帖子/视频等）
     */
    private Long contentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 状态（1正常 0删除）
     */
    private Integer status;
}