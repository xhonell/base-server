package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * program: BaseServer
 * ClassName ExamOption
 * description: 选项实体类
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_exam_option")
public class ExamOption {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题目ID
     */
    @TableField("question_id")
    private Long questionId;

    /**
     * 选项内容
     */
    private String content;

    /**
     * 选项标识（A B C D）
     */
    private String optionLabel;

    /**
     * 是否正确答案
     */
    private Boolean isCorrect;

    /**
     * 选项排序
     */
    private Integer sortOrder;
}