package com.xhonell.common.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * program: BaseServer
 * ClassName LikeRequest
 * description: 点赞请求类
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Data
public class LikeRequest {

    /**
     * 内容ID
     */
    @NotNull(message = "内容ID不能为空")
    private Long contentId;

    /**
     * 点赞操作类型（1点赞 0取消点赞）
     */
    @NotNull(message = "点赞操作类型不能为空")
    private Integer operation;
}