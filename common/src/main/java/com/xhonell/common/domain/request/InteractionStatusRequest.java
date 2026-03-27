package com.xhonell.common.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * program: BaseServer
 * ClassName InteractionStatusRequest
 * description: 互动状态请求类
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Data
public class InteractionStatusRequest {

    /**
     * 内容ID
     */
    @NotNull(message = "内容ID不能为空")
    private Long contentId;
}