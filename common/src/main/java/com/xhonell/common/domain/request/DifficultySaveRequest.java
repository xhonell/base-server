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
 * ClassName DifficultySaveRequest
 * description: 难度保存请求
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DifficultySaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 难度名称
     */
    private String name;

    /**
     * 难度说明
     */
    private String description;

    /**
     * 对应积分
     */
    private Integer score;

    /**
     * 星级
     */
    private Integer starts;

    /**
     * 状态（1 启用，0 禁用）
     */
    private Byte status;
}