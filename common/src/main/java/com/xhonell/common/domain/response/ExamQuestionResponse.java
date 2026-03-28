package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ExamQuestionResponse
 * description: 题目响应
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
public class ExamQuestionResponse {

    /**
     * 题目ID
     */
    private Long id;

    /**
     * 题目类型（1 单选题 2 多选题 3 判断题 4 填空题 5 简答题）
     */
    private Integer type;

    /**
     * 题目类型名称
     */
    private String typeName;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目分值
     */
    private Integer score;

    /**
     * 题目排序
     */
    private Integer sortOrder;

    /**
     * 参考答案
     */
    private String answer;

    /**
     * 题目解析
     */
    private String analysis;

    /**
     * 选项列表
     */
    private List<ExamOptionResponse> options;
}