package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName ExamRecordResponse
 * description: 考试记录响应
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
public class ExamRecordResponse {

    /**
     * 考试记录ID
     */
    private Long id;

    /**
     * 试卷ID
     */
    private Long paperId;

    /**
     * 试卷名称
     */
    private String paperTitle;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 答题数量
     */
    private Integer answerCount;

    /**
     * 正确数量
     */
    private Integer correctCount;

    /**
     * 及格状态（0 不及格 1 及格）
     */
    private Integer passStatus;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 耗时（秒）
     */
    private Integer duration;
}