package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.Article;
import com.xhonell.common.domain.request.ArticlePageRequest;
import com.xhonell.common.domain.response.ArticlePageResponse;
import com.xhonell.common.domain.response.RecommendResponse;

/**
 * program: BaseServer
 * ClassName ArticleService
 * description: 文章Service接口
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
public interface ArticleService extends IService<Article> {

    /**
     * 文章分页查询
     * @param request 查询请求
     * @return 分页结果
     */
    ArticlePageResponse pageArticle(ArticlePageRequest request);

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    RecommendResponse getArticleDetail(Long id);

    /**
     * 根据内容ID保存文章
     */
    void saveByContentId(Long contentId, String content, String author, String source);

    /**
     * 根据内容ID更新文章
     */
    void updateByContentId(Long contentId, String content, String author, String source);

    /**
     * 根据内容ID删除文章
     */
    void removeByContentId(Long contentId);
}