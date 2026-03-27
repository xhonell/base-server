package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Comment;
import com.xhonell.common.domain.request.CommentPageRequest;
import com.xhonell.common.domain.request.CommentSaveRequest;
import com.xhonell.common.domain.response.CommentResponse;

/**
 * @author xhonell
 * @date 2026/3/26
 * @desc
 */
public interface CommentService extends IService<Comment> {

    /**
     * 发布评论
     *
     * @param request 评论保存请求
     */
    void publishComment(CommentSaveRequest request);

    /**
     * 删除评论（软删除）
     *
     * @param id 评论ID
     */
    void deleteComment(Long id);

    /**
     * 获取文章评论分页
     *
     * @param request 评论分页请求
     * @return 评论分页结果
     */
    PageInfo<CommentResponse> getCommentPage(CommentPageRequest request);
}
