package com.xhonell.common.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * program: BaseServer
 * ClassName CollectRequest
 * description: 收藏请求类
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Data
public class CollectRequest {

    /**
     * 内容ID
     */
    @NotNull(message = "内容ID不能为空")
    private Long contentId;

    /**
     * 收藏操作类型（1收藏 0取消收藏）
     */
    @NotNull(message = "收藏操作类型不能为空")
    private Integer operation;
}