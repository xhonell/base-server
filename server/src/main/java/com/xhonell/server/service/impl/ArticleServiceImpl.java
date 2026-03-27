package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.Article;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.request.ArticlePageRequest;
import com.xhonell.common.domain.response.ArticlePageResponse;
import com.xhonell.common.domain.response.RecommendResponse;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.server.mapper.ArticleMapper;
import com.xhonell.server.service.ArticleService;
import com.xhonell.server.service.ContentCategoryService;
import com.xhonell.server.service.ContentService;
import com.xhonell.server.service.FileService;
import com.xhonell.server.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private final ArticleMapper articleMapper;
    private final ContentService contentService;
    private final ContentCategoryService contentCategoryService;
    private final FileService fileService;
    private final TagService tagService;
    private final com.xhonell.server.service.DifficultyService difficultyService;
    private final com.xhonell.server.service.LikeService likeService;
    private final com.xhonell.server.service.CollectService collectService;
    private final com.xhonell.server.mapper.ViewRecordMapper viewRecordMapper;

    @Override
    public ArticlePageResponse pageArticle(ArticlePageRequest request) {
        // 开始分页
        PageUtils.startPage(request.getPage(), request.getPageSize());

        // 构建查询条件 - 首先查询文章内容
        LambdaQueryWrapper<Content> contentQueryWrapper = new LambdaQueryWrapper<>();
        contentQueryWrapper.eq(Content::getType, (byte) 1) // 文章类型
                .eq(Content::getStatus, 1); // 启用状态

        // 分类ID筛选
        if (request.getCategoryId() != null) {
            contentQueryWrapper.eq(Content::getCategoryId, request.getCategoryId());
        }

        // 排序
        switch (request.getSortBy()) {
            case 1: // 最新发布
                contentQueryWrapper.orderByDesc(Content::getCreateTime);
                break;
            case 2: // 最多点赞
                contentQueryWrapper.orderByDesc(Content::getLikeCount);
                break;
            case 3: // 最多阅读
                contentQueryWrapper.orderByDesc(Content::getViewCount);
                break;
            default:
                contentQueryWrapper.orderByDesc(Content::getCreateTime);
                break;
        }

        // 执行查询
        List<Content> contents = contentService.list(contentQueryWrapper);

        // 转换为RecommendResponse
        List<RecommendResponse> articles = contents.stream()
                .map(this::convertToRecommendResponse)
                .toList();

        // 获取分页信息
        int total = contents.size();
        int start = (request.getPage() - 1) * request.getPageSize();
        int end = Math.min(start + request.getPageSize(), total);

        List<RecommendResponse> pagedArticles = articles.subList(start, end);

        // 构建返回结果
        return new ArticlePageResponse(
                request.getPage(),
                request.getPageSize(),
                (long) total,
                (int) Math.ceil((double) total / request.getPageSize()),
                pagedArticles
        );
    }

    @Override
    public RecommendResponse getArticleDetail(Long id) {
        Content content = contentService.getById(id);
        if (content == null || content.getType() != 1 || content.getStatus() != 1) {
            return null; // 或者抛出异常
        }

        // 增加观看数
        LambdaUpdateWrapper<Content> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Content::getId, id);
        updateWrapper.set(Content::getViewCount, content.getViewCount() == null ? 1 : content.getViewCount() + 1);
        contentService.update(updateWrapper);

        // 添加观看记录
        try {
            Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
            com.xhonell.common.domain.entity.ViewRecord viewRecord = new com.xhonell.common.domain.entity.ViewRecord();
            viewRecord.setUserId(userId);
            viewRecord.setContentId(id);
            viewRecord.setType((byte) 1); // 文章类型
            viewRecord.setCreateTime(java.time.LocalDateTime.now());
            viewRecordMapper.insert(viewRecord);
        } catch (Exception e) {
            // 用户未登录时忽略观看记录
        }

        RecommendResponse response = convertToRecommendResponse(content);

        // 检查是否已点赞
        try {
            Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
            Boolean isLiked = likeService.isLiked(id, userId);
            response.setIsLiked(isLiked);
        } catch (Exception e) {
            // 如果用户未登录，设置为未点赞
            response.setIsLiked(false);
        }

        // 检查是否已收藏
        try {
            Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
            Boolean isCollected = collectService.isCollected(id, userId);
            response.setIsCollected(isCollected);
        } catch (Exception e) {
            // 如果用户未登录，设置为未收藏
            response.setIsCollected(false);
        }

        return response;
    }

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

    /**
     * 将Content实体转换为RecommendResponse
     */
    private RecommendResponse convertToRecommendResponse(Content content) {
        RecommendResponse response = new RecommendResponse();
        response.setId(content.getId());
        response.setTitle(content.getTitle());
        response.setType(content.getType());
        response.setCategoryId(content.getCategoryId());
        response.setDescription(content.getDescription());
        response.setFileId(content.getFileId());
        response.setViewCount(content.getViewCount());
        response.setLikeCount(content.getLikeCount());
        response.setCollectCount(content.getCollectCount());
        response.setCreateTime(content.getCreateTime());
        response.setTagId(content.getTagId());

        // 设置文件URL
        if (content.getFileId() != null) {
            File file = fileService.getById(content.getFileId());
            if (file != null) {
                response.setFileUrl(file.getFilePathUrl());
            }
        }

        // 从标签服务获取标签名称
        if (content.getTagId() != null) {
            com.xhonell.common.domain.entity.Tag tag = tagService.getById(content.getTagId());
            if (tag != null) {
                response.setTagName(tag.getName());
            }
        }

        // 从难度服务获取难度信息
        if (content.getDifficultyId() != null) {
            com.xhonell.common.domain.entity.Difficulty difficulty = difficultyService.getById(content.getDifficultyId());
            if (difficulty != null) {
                response.setDifficultyName(difficulty.getName());
                response.setDifficultyScore(difficulty.getScore());
            }
        }

        // 丰富分类信息
        if (content.getCategoryId() != null) {
            com.xhonell.common.domain.entity.ContentCategory category = contentCategoryService.getById(content.getCategoryId());
            if (category != null) {
                response.setCategoryName(category.getCategoryName());
            }
        }

        // 丰富文章信息
        enrichWithArticleInfo(response, content.getId());

        return response;
    }

    /**
     * 丰富文章信息
     */
    private void enrichWithArticleInfo(RecommendResponse response, Long contentId) {
        // 查询文章信息
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Article::getContentId, contentId);
        Article article = articleMapper.selectOne(articleQuery);

        if (article != null) {
            response.setContent(article.getContent());
            response.setAuthor(article.getAuthor());
            response.setSource(article.getSource());
        }
    }
}