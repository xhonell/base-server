package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName TopicPageRequest
 * description: 话题分页请求类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicPageRequest extends BasePageRequest {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 分类（1谈论 2问答 3分享 4活动）
     */
    private Integer category;

    /**
     * 状态（1正常 0删除）
     */
    private Integer status;
}