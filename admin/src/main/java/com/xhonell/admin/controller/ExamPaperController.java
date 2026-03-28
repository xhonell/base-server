package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.ExamPaperService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.ExamPaperPageRequest;
import com.xhonell.common.domain.request.ExamPaperSaveRequest;
import com.xhonell.common.domain.response.ExamPaperResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName ExamPaperController
 * description: 试卷管理Controller
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@RestController
@RequestMapping("/exam/paper")
@RequiredArgsConstructor
public class ExamPaperController {

    private final ExamPaperService examPaperService;

    /**
     * 分页查询试卷列表
     *
     * @param request 查询请求
     * @return 试卷列表
     */
    @GetMapping("/list")
    @RequirePermission("admin:exam:list")
    public Result<PageInfo<ExamPaperResponse>> list(ExamPaperPageRequest request) {
        return Result.success(examPaperService.pageList(request));
    }

    /**
     * 获取试卷详情
     *
     * @param paperId 试卷ID
     * @return 试卷详情
     */
    @GetMapping("/detail/{paperId}")
    @RequirePermission("admin:exam:detail")
    public Result<ExamPaperResponse> detail(@PathVariable Long paperId) {
        return Result.success(examPaperService.getDetail(paperId));
    }

    /**
     * 保存试卷
     *
     * @param request 保存请求
     * @return 响应
     */
    @PostMapping("/save")
    @RequirePermission("admin:exam:save")
    public Result<Void> save(@RequestBody ExamPaperSaveRequest request) {
        examPaperService.savePaper(request);
        return Result.success();
    }

    /**
     * 删除试卷
     *
     * @param paperId 试卷ID
     * @return 响应
     */
    @DeleteMapping("/delete/{paperId}")
    @RequirePermission("admin:exam:delete")
    public Result<Void> delete(@PathVariable Long paperId) {
        examPaperService.deletePaper(paperId);
        return Result.success();
    }

    /**
     * 发布/取消发布试卷
     *
     * @param paperId 试卷ID
     * @param status  状态（0 未发布 1 已发布）
     * @return 响应
     */
    @PutMapping("/status/{paperId}/{status}")
    @RequirePermission("admin:exam:status")
    public Result<Void> updateStatus(@PathVariable Long paperId, @PathVariable Integer status) {
        examPaperService.updateStatus(paperId, status);
        return Result.success();
    }
}