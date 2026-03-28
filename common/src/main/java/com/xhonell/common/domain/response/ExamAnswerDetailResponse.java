package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName ExamAnswerDetailResponse
 * description: 答卷详情响应
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
public class ExamAnswerDetailResponse {

    /**
     * 答题记录ID
     */
    private Long id;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 题目类型（1 单选题 2 多选题 3 判断题 4 填空题 5 简答题）
     */
    private Integer type;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 选项列表
     */
    private java.util.List<ExamOptionResponse> options;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 正确答案
     */
    private String correctAnswer;

    /**
     * 是否正确
     */
    private Boolean isCorrect;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 题目解析
     */
    private String analysis;
}