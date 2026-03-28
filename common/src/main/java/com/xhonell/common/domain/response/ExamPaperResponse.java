package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * program: BaseServer
 * ClassName ExamPaperResponse
 * description: 试卷响应
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
public class ExamPaperResponse {

    /**
     * 试卷ID
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
     * 试卷总分
     */
    private Integer totalScore;

    /**
     * 及格分数
     */
    private Integer passScore;

    /**
     * 考试时长（分钟）
     */
    private Integer duration;

    /**
     * 试卷状态（0 未发布 1 已发布）
     */
    private Integer status;

    /**
     * 题目数量
     */
    private Integer questionCount;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 题目列表（不含答案）
     */
    private List<ExamQuestionResponse> questions;

    /**
     * 是否已答题（用户端用）
     */
    private Boolean isAnswered;
}