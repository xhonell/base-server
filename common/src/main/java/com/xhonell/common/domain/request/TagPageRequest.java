package com.xhonell.common.domain.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName TagPageRequest
 * description: 标签分页请求类
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TagPageRequest extends BasePageRequest {

    /**
     * 标签名称查询
     */
    private String name;

    /**
     * 适用年龄段ID查询
     */
    private Long ageRangeId;

    /**
     * 状态查询
     */
    private Byte status;
}