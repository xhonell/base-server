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
 * ClassName ExamRecord
 * description: 考试记录实体类
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_exam_record")
public class ExamRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 试卷ID
     */
    @TableField("paper_id")
    private Long paperId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

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
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;

    /**
     * 耗时（秒）
     */
    private Integer duration;
}