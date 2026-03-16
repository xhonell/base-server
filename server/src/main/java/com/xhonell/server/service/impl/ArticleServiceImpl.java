package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.Article;
import com.xhonell.server.mapper.ArticleMapper;
import com.xhonell.server.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName ArticleServiceImpl
 * description: 文章Service实现
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public void saveByContentId(Long contentId, String content, String author, String source) {
        Article article = new Article();
        article.setContentId(contentId);
        article.setContent(content);
        article.setAuthor(author);
        article.setSource(source);
        save(article);
    }

    @Override
    public void updateByContentId(Long contentId, String content, String author, String source) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getContentId, contentId);
        Article article = getOne(queryWrapper);

        if (article != null) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getContentId, contentId);
            if (content != null) {
                updateWrapper.set(Article::getContent, content);
            }
            if (author != null) {
                updateWrapper.set(Article::getAuthor, author);
            }
            if (source != null) {
                updateWrapper.set(Article::getSource, source);
            }
            update(updateWrapper);
        }
    }

    @Override
    public void removeByContentId(Long contentId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getContentId, contentId);
        remove(queryWrapper);
    }
}