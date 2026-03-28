package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * program: BaseServer
 * ClassName ExamAnswerItemRequest
 * description: 答题项请求
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamAnswerItemRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 用户答案
     */
    private String answer;
}