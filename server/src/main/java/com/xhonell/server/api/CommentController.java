package com.xhonell.server.api;

import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.CommentPageRequest;
import com.xhonell.common.domain.request.CommentSaveRequest;
import com.xhonell.common.domain.response.CommentResponse;
import com.xhonell.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName CommentController
 * description: 评论相关接口
 * author: xhonell
 * create: 2026/3/26
 * Version 1.0
 **/
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 发布评论
     * @param request 评论保存请求
     * @return 无返回
     */
    @PostMapping("/publish")
    public Result<Void> publishComment(@RequestBody CommentSaveRequest request) {
        commentService.publishComment(request);
        return Result.success();
    }

    /**
     * 删除评论
     * @param id 评论ID
     * @return 无返回
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }

    /**
     * 获取评论分页
     * @param request 评论分页请求
     * @return 评论分页结果
     */
    @PostMapping("/page")
    public Result<PageInfo<CommentResponse>> getCommentPage(@RequestBody CommentPageRequest request) {
        PageInfo<CommentResponse> pageInfo = commentService.getCommentPage(request);
        return Result.success(pageInfo);
    }
}