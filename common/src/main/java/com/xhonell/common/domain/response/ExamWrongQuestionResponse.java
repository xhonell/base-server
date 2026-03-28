package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName ExamWrongQuestionResponse
 * description: 错题本响应
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
public class ExamWrongQuestionResponse {

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 题目类型
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
     * 选项列表
     */
    private java.util.List<ExamOptionResponse> options;

    /**
     * 正确答案
     */
    private String correctAnswer;

    /**
     * 题目解析
     */
    private String analysis;

    /**
     * 错误次数
     */
    private Integer wrongCount;

    /**
     * 最后错误时间
     */
    private String lastWrongTime;
}