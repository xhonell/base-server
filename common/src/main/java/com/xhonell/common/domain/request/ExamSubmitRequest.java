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
 * ClassName ExamSubmitRequest
 * description: 提交答卷请求
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamSubmitRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 试卷ID
     */
    private Long paperId;

    /**
     * 答题列表
     */
    private List<ExamAnswerItemRequest> answers;
}