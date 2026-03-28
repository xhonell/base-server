package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName TopicCommentPageRequest
 * description: 话题评论分页请求类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicCommentPageRequest extends BasePageRequest {

    /**
     * 话题ID
     */
    private Long topicId;
}