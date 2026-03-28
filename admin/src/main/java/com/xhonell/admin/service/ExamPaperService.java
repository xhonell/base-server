package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.ExamPaper;
import com.xhonell.common.domain.request.ExamPaperPageRequest;
import com.xhonell.common.domain.request.ExamPaperSaveRequest;
import com.xhonell.common.domain.response.ExamPaperResponse;

/**
 * program: BaseServer
 * ClassName ExamPaperService
 * description: 试卷Service
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
public interface ExamPaperService extends IService<ExamPaper> {

    /**
     * 分页查询试卷列表
     *
     * @param request 查询请求
     * @return 试卷列表
     */
    PageInfo<ExamPaperResponse> pageList(ExamPaperPageRequest request);

    /**
     * 获取试卷详情（含题目）
     *
     * @param paperId 试卷ID
     * @return 试卷详情
     */
    ExamPaperResponse getDetail(Long paperId);

    /**
     * 保存试卷
     *
     * @param request 保存请求
     */
    void savePaper(ExamPaperSaveRequest request);

    /**
     * 删除试卷
     *
     * @param paperId 试卷ID
     */
    void deletePaper(Long paperId);

    /**
     * 发布/取消发布试卷
     *
     * @param paperId 试卷ID
     * @param status  状态（0 未发布 1 已发布）
     */
    void updateStatus(Long paperId, Integer status);
}