package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.ExamRecord;
import com.xhonell.common.domain.request.ExamSubmitRequest;
import com.xhonell.common.domain.response.ExamPaperResponse;
import com.xhonell.common.domain.response.ExamRecordResponse;
import com.xhonell.common.domain.response.ExamWrongQuestionResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ExamService
 * description: 考试Service（用户端）
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
public interface ExamService extends IService<ExamRecord> {

    /**
     * 分页查询已发布的试卷列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 试卷列表
     */
    PageInfo<ExamPaperResponse> pageList(Integer page, Integer pageSize);

    /**
     * 获取试卷详情（不含答案）
     *
     * @param paperId 试卷ID
     * @return 试卷详情
     */
    ExamPaperResponse getPaperDetail(Long paperId);

    /**
     * 提交答卷
     *
     * @param request 提交请求
     * @return 考试记录ID
     */
    Long submitExam(ExamSubmitRequest request);

    /**
     * 分页查询用户的考试记录
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 考试记录列表
     */
    PageInfo<ExamRecordResponse> getMyRecords(Integer page, Integer pageSize);

    /**
     * 获取答卷详情（含答案）
     *
     * @param recordId 考试记录ID
     * @return 答卷详情
     */
    List<com.xhonell.common.domain.response.ExamAnswerDetailResponse> getRecordDetail(Long recordId);

    /**
     * 获取错题本
     *
     * @return 错题列表
     */
    List<ExamWrongQuestionResponse> getWrongQuestions();
}