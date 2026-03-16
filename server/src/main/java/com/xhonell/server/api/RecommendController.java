package com.xhonell.server.api;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.RecommendRequest;
import com.xhonell.common.domain.response.RecommendResponse;
import com.xhonell.server.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName RecommendController
 * description: 推荐算法Controller - 为前端用户提供推荐内容
 * author: xhonell
 * create: 2026年3月15日
 * Version 1.0
 **/
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    /**
     * 根据推荐算法获取推荐内容列表
     * @return 推荐内容列表
     */
    @PostMapping("/list")
    public Result<List<RecommendResponse>> recommend() {
        List<RecommendResponse> recommendations = recommendService.recommend(new RecommendRequest());
        return Result.success(recommendations);
    }

    /**
     * 获取推荐文章列表
     * @return 推荐文章列表
     */
    @PostMapping("/articles")
    public Result<List<RecommendResponse>> recommendArticles() {
        List<RecommendResponse> recommendations = recommendService.recommend(new RecommendRequest().setType((byte) 1));
        return Result.success(recommendations);
    }

    /**
     * 获取推荐视频列表
     * @return 推荐视频列表
     */
    @PostMapping("/videos")
    public Result<List<RecommendResponse>> recommendVideos() {
        List<RecommendResponse> recommendations = recommendService.recommend(new RecommendRequest().setType((byte) 2));
        return Result.success(recommendations);
    }
    
    /**
     * 获取近一周热门文章列表（如果不足则扩展时间范围）
     * @return 热门文章列表（最多2篇）
     */
    @GetMapping("/weekly-hot-articles")
    public Result<List<RecommendResponse>> getWeeklyHotArticles() {
        List<RecommendResponse> recommendations = recommendService.getWeeklyHotArticles();
        return Result.success(recommendations);
    }
}