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
 * ClassName ExamPaperSaveRequest
 * description: 试卷保存请求
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamPaperSaveRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 试卷ID（更新时需要）
     */
    private Long id;

    /**
     * 试卷名称
     */
    private String title;

    /**
     * 试卷描述
     */
    private String description;

    /**
     * 考试时长（分钟）
     */
    private Integer duration;

    /**
     * 及格分数
     */
    private Integer passScore;

    /**
     * 题目列表
     */
    private List<ExamQuestionSaveRequest> questions;
}