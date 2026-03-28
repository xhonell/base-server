package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName ExamAnswer
 * description: 答题记录实体类
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_exam_answer")
public class ExamAnswer {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 考试记录ID
     */
    @TableField("record_id")
    private Long recordId;

    /**
     * 题目ID
     */
    @TableField("question_id")
    private Long questionId;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 是否正确
     */
    private Boolean isCorrect;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 答题时间
     */
    @TableField("answer_time")
    private LocalDateTime answerTime;
}