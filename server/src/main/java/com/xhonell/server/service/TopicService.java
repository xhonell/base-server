package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Topic;
import com.xhonell.common.domain.request.TopicCommentPageRequest;
import com.xhonell.common.domain.request.TopicCommentRequest;
import com.xhonell.common.domain.request.TopicPageRequest;
import com.xhonell.common.domain.request.TopicSaveRequest;
import com.xhonell.common.domain.response.TopicCommentResponse;
import com.xhonell.common.domain.response.TopicResponse;

/**
 * program: BaseServer
 * ClassName TopicService
 * description: 话题服务接口
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
public interface TopicService extends IService<Topic> {

    /**
     * 发布话题
     *
     * @param request 话题保存请求
     * @return 话题ID
     */
    Long publishTopic(TopicSaveRequest request);

    /**
     * 查看话题详情（查看数+1）
     *
     * @param id 话题ID
     * @return 话题详情
     */
    TopicResponse getTopicDetail(Long id);

    /**
     * 获取话题分页
     *
     * @param request 话题分页请求
     * @return 话题分页结果
     */
    PageInfo<TopicResponse> getTopicPage(TopicPageRequest request);

    /**
     * 发布话题评论
     *
     * @param request 话题评论请求
     */
    void publishTopicComment(TopicCommentRequest request);

    /**
     * 删除话题评论（软删除，回复数-1）
     *
     * @param id 评论ID
     */
    void deleteTopicComment(Long id);

    /**
     * 获取话题评论分页
     *
     * @param request 话题评论分页请求
     * @return 评论分页结果
     */
    PageInfo<TopicCommentResponse> getTopicCommentPage(TopicCommentPageRequest request);
}