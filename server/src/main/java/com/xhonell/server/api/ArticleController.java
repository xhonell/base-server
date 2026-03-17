package com.xhonell.server.api;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.ArticlePageRequest;
import com.xhonell.common.domain.response.ArticlePageResponse;
import com.xhonell.common.domain.response.RecommendResponse;
import com.xhonell.server.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName ArticleController
 * description: 文章相关接口
 * author: xhonell
 * create: 2026年3月17日
 * Version 1.0
 **/
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 文章分页查询
     * @param request 查询请求
     * @return 分页结果
     */
    @PostMapping("/page")
    public Result<ArticlePageResponse> pageArticle(@RequestBody ArticlePageRequest request) {
        ArticlePageResponse response = articleService.pageArticle(request);
        return Result.success(response);
    }

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/detail/{id}")
    public Result<RecommendResponse> getArticleDetail(@PathVariable Long id) {
        RecommendResponse response = articleService.getArticleDetail(id);
        return Result.success(response);
    }
}