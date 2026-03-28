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
 * ClassName ExamPaper
 * description: 试卷实体类
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_exam_paper")
public class ExamPaper {

    @TableId(type = IdType.AUTO)
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
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}