package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName ExamOptionResponse
 * description: 选项响应
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamOptionResponse {

    /**
     * 选项ID
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