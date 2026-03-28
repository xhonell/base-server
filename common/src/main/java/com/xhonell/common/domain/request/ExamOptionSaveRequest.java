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
 * ClassName ExamOptionSaveRequest
 * description: 选项保存请求
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamOptionSaveRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 选项ID（更新时需要）
     */
    private Long id;

    /**
     * 选项内容
     */
    private String content;

    /**
     * 选项标识（A B C D）
     */
    private String optionLabel;

    /**
     * 是否正确答案
     */
    private Boolean isCorrect;

    /**
     * 选项排序
     */
    private Integer sortOrder;
}