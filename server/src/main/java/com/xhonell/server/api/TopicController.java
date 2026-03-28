package com.xhonell.server.api;

import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.TopicCommentPageRequest;
import com.xhonell.common.domain.request.TopicCommentRequest;
import com.xhonell.common.domain.request.TopicPageRequest;
import com.xhonell.common.domain.request.TopicSaveRequest;
import com.xhonell.common.domain.response.TopicCommentResponse;
import com.xhonell.common.domain.response.TopicResponse;
import com.xhonell.server.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName TopicController
 * description: 话题相关接口
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    /**
     * 发布话题
     * @param request 话题保存请求
     * @return 话题ID
     */
    @PostMapping("/publish")
    public Result<Long> publishTopic(@RequestBody TopicSaveRequest request) {
        Long topicId = topicService.publishTopic(request);
        return Result.success(topicId);
    }

    /**
     * 查看话题详情
     * @param id 话题ID
     * @return 话题详情
     */
    @GetMapping("/{id}")
    public Result<TopicResponse> getTopicDetail(@PathVariable Long id) {
        TopicResponse response = topicService.getTopicDetail(id);
        return Result.success(response);
    }

    /**
     * 获取话题分页
     * @param request 话题分页请求
     * @return 话题分页结果
     */
    @PostMapping("/page")
    public Result<PageInfo<TopicResponse>> getTopicPage(@RequestBody TopicPageRequest request) {
        PageInfo<TopicResponse> pageInfo = topicService.getTopicPage(request);
        return Result.success(pageInfo);
    }

    /**
     * 发布话题评论
     * @param request 话题评论请求
     * @return 无返回
     */
    @PostMapping("/comment/publish")
    public Result<Void> publishTopicComment(@RequestBody TopicCommentRequest request) {
        topicService.publishTopicComment(request);
        return Result.success();
    }

    /**
     * 删除话题评论
     * @param id 评论ID
     * @return 无返回
     */
    @DeleteMapping("/comment/{id}")
    public Result<Void> deleteTopicComment(@PathVariable Long id) {
        topicService.deleteTopicComment(id);
        return Result.success();
    }

    /**
     * 获取话题评论分页
     * @param request 话题评论分页请求
     * @return 评论分页结果
     */
    @PostMapping("/comment/page")
    public Result<PageInfo<TopicCommentResponse>> getTopicCommentPage(@RequestBody TopicCommentPageRequest request) {
        PageInfo<TopicCommentResponse> pageInfo = topicService.getTopicCommentPage(request);
        return Result.success(pageInfo);
    }
}