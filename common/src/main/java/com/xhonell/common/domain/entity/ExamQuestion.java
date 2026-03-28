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
 * ClassName ExamQuestion
 * description: 题目实体类
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_exam_question")
public class ExamQuestion {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 试卷ID
     */
    @TableField("paper_id")
    private Long paperId;

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