package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.Article;

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