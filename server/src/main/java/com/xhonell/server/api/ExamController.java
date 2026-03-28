package com.xhonell.server.api;

import com.github.pagehelper.PageInfo;
import com.xhonell.common.annotation.NoAuth;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.ExamSubmitRequest;
import com.xhonell.common.domain.response.ExamAnswerDetailResponse;
import com.xhonell.common.domain.response.ExamPaperResponse;
import com.xhonell.common.domain.response.ExamRecordResponse;
import com.xhonell.common.domain.response.ExamWrongQuestionResponse;
import com.xhonell.server.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ExamController
 * description: 考试相关接口（用户端）
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    /**
     * 分页查询已发布的试卷列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 试卷列表
     */
    @GetMapping("/list")
    public Result<PageInfo<ExamPaperResponse>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(examService.pageList(page, pageSize));
    }

    /**
     * 获取试卷详情（不含答案）
     *
     * @param paperId 试卷ID
     * @return 试卷详情
     */
    @GetMapping("/paper/{paperId}")
    @NoAuth
    public Result<ExamPaperResponse> getPaperDetail(@PathVariable Long paperId) {
        return Result.success(examService.getPaperDetail(paperId));
    }

    /**
     * 提交答卷
     *
     * @param request 提交请求
     * @return 考试记录ID
     */
    @PostMapping("/submit")
    public Result<Long> submit(@RequestBody ExamSubmitRequest request) {
        return Result.success(examService.submitExam(request));
    }

    /**
     * 分页查询我的考试记录
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 考试记录列表
     */
    @GetMapping("/my/records")
    public Result<PageInfo<ExamRecordResponse>> getMyRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(examService.getMyRecords(page, pageSize));
    }

    /**
     * 获取答卷详情（含答案）
     *
     * @param recordId 考试记录ID
     * @return 答卷详情
     */
    @GetMapping("/record/{recordId}")
    public Result<List<ExamAnswerDetailResponse>> getRecordDetail(@PathVariable Long recordId) {
        return Result.success(examService.getRecordDetail(recordId));
    }

    /**
     * 获取错题本
     *
     * @return 错题列表
     */
    @GetMapping("/wrong/questions")
    public Result<List<ExamWrongQuestionResponse>> getWrongQuestions() {
        return Result.success(examService.getWrongQuestions());
    }
}