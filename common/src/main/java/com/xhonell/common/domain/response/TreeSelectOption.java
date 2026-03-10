package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * program: BaseServer
 * ClassName TreeSelectOption
 * description: 树形下拉选项响应类
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeSelectOption implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 选项值
     */
    private Long value;

    /**
     * 选项标签
     */
    private String label;

    /**
     * 子选项
     */
    private List<TreeSelectOption> children;
}