package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.ExamRecord;
import com.xhonell.common.domain.response.ExamAnswerDetailResponse;
import com.xhonell.common.domain.response.ExamRankingResponse;
import com.xhonell.common.domain.response.ExamRecordResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ExamRecordService
 * description: 考试记录Service
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
public interface ExamRecordService extends IService<ExamRecord> {

    /**
     * 分页查询考试记录列表
     *
     * @param paperId 试卷ID
     * @param page    页码
     * @param pageSize 每页数量
     * @return 考试记录列表
     */
    PageInfo<ExamRecordResponse> pageList(Long paperId, Integer page, Integer pageSize);

    /**
     * 获取试卷排名
     *
     * @param paperId 试卷ID
     * @return 排名列表
     */
    List<ExamRankingResponse> getRanking(Long paperId);

    /**
     * 获取答卷详情
     *
     * @param recordId 考试记录ID
     * @return 答卷详情
     */
    List<ExamAnswerDetailResponse> getAnswerDetail(Long recordId);
}