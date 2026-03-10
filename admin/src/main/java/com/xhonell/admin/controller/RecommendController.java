package com.xhonell.admin.controller;

import com.xhonell.admin.service.RecommendService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.RecommendRequest;
import com.xhonell.common.domain.response.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName RecommendController
 * description: 推荐算法Controller
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    /**
     * 获取推荐内容列表
     *
     * @param request 推荐请求参数
     * @return 推荐内容列表
     */
    @PostMapping("/list")
    public Result<List<RecommendResponse>> recommend(@RequestBody RecommendRequest request) {
        List<RecommendResponse> recommendations = recommendService.recommend(request);
        return Result.success(recommendations);
    }

    /**
     * 获取推荐文章列表
     *
     * @param request 推荐请求参数
     * @return 推荐文章列表
     */
    @PostMapping("/articles")
    public Result<List<RecommendResponse>> recommendArticles(@RequestBody RecommendRequest request) {
        request.setType((byte) 1);
        List<RecommendResponse> recommendations = recommendService.recommend(request);
        return Result.success(recommendations);
    }

    /**
     * 获取推荐视频列表
     *
     * @param request 推荐请求参数
     * @return 推荐视频列表
     */
    @PostMapping("/videos")
    public Result<List<RecommendResponse>> recommendVideos(@RequestBody RecommendRequest request) {
        request.setType((byte) 2);
        List<RecommendResponse> recommendations = recommendService.recommend(request);
        return Result.success(recommendations);
    }
}