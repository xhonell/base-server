package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.ExamRecordService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.response.ExamAnswerDetailResponse;
import com.xhonell.common.domain.response.ExamRankingResponse;
import com.xhonell.common.domain.response.ExamRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ExamRecordController
 * description: 考试记录管理Controller
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@RestController
@RequestMapping("/exam/record")
@RequiredArgsConstructor
public class ExamRecordController {

    private final ExamRecordService examRecordService;

    /**
     * 分页查询考试记录列表
     *
     * @param paperId 试卷ID
     * @param page    页码
     * @param pageSize 每页数量
     * @return 考试记录列表
     */
    @GetMapping("/list")
    @RequirePermission("admin:exam:record:list")
    public Result<PageInfo<ExamRecordResponse>> list(
            @RequestParam(required = false) Long paperId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(examRecordService.pageList(paperId, page, pageSize));
    }

    /**
     * 获取试卷排名
     *
     * @param paperId 试卷ID
     * @return 排名列表
     */
    @GetMapping("/ranking/{paperId}")
    @RequirePermission("admin:exam:ranking")
    public Result<List<ExamRankingResponse>> ranking(@PathVariable Long paperId) {
        return Result.success(examRecordService.getRanking(paperId));
    }

    /**
     * 获取答卷详情
     *
     * @param recordId 考试记录ID
     * @return 答卷详情
     */
    @GetMapping("/answer/{recordId}")
    @RequirePermission("admin:exam:answer:detail")
    public Result<List<ExamAnswerDetailResponse>> answerDetail(@PathVariable Long recordId) {
        return Result.success(examRecordService.getAnswerDetail(recordId));
    }
}