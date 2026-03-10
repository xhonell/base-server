package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * program: BaseServer
 * ClassName TagSaveRequest
 * description: 标签保存请求
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagSaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 适用年龄段ID
     */
    private Long ageRangeId;

    /**
     * 状态（1 启用，0 禁用）
     */
    private Byte status;
}