package com.xhonell.common.domain.request;

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
 * ClassName ExamQuestionSaveRequest
 * description: 题目保存请求
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamQuestionSaveRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 题目ID（更新时需要）
     */
    private Long id;

    /**
     * 题目类型（1 单选题 2 多选题 3 判断题 4 填空题 5 简答题）
     */
    private Integer type;

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
     * 选项列表（单选、多选题需要）
     */
    private List<ExamOptionSaveRequest> options;
}