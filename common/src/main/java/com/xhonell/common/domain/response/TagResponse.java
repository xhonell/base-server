package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName TagResponse
 * description: 标签响应类，用于返回标签相关信息及关联信息
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagResponse {

    /**
     * 主键ID
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
     * 适用年龄段名称
     */
    private String ageRangeName;

    /**
     * 状态（1 启用，0 禁用）
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}