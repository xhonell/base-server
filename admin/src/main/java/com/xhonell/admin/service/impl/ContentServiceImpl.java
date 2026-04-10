package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.ContentMapper;
import com.xhonell.admin.service.ArticleService;
import com.xhonell.admin.service.ContentCategoryService;
import com.xhonell.admin.service.ContentService;
import com.xhonell.admin.service.DifficultyService;
import com.xhonell.admin.service.FileService;
import com.xhonell.admin.service.PoliticService;
import com.xhonell.admin.service.TagService;
import com.xhonell.admin.service.VideoService;
import com.xhonell.common.domain.entity.Article;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.entity.ContentCategory;
import com.xhonell.common.domain.entity.Difficulty;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.entity.Tag;
import com.xhonell.common.domain.entity.Video;
import com.xhonell.common.domain.request.ContentPageRequest;
import com.xhonell.common.domain.request.ContentSaveRequest;
import com.xhonell.common.domain.response.ContentResponse;
import com.xhonell.common.utils.PageUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName ContentServiceImpl
 * description: 教育内容Service实现
 * author: xhonell
 * create: 2026年1月18日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {

    private final ContentCategoryService contentCategoryService;
    private final DifficultyService difficultyService;
    private final FileService fileService;
    private final PoliticService politicService;
    private final TagService tagService;
    private final ArticleService articleService;
    private final VideoService videoService;

    @Override
    public PageInfo<ContentResponse> selectList(Integer page, Integer pageSize) {
        PageUtils.startPage(page, pageSize);
        List<Content> contents = selectListBy();
        List<ContentResponse> contentResponses = convertToResponseList(contents);
        return PageUtils.toPageInfo(contentResponses);
    }

    @Override
    public PageInfo<ContentResponse> selectListByRequest(ContentPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<Content> contents = selectListByRequestParams(request);
        List<ContentResponse> contentResponses = convertToResponseList(contents);
        return PageUtils.toPageInfo(contentResponses);
    }

    @Override
    public void saveBy(ContentSaveRequest request) {
        Content content = new Content();
        content.setTitle(request.getTitle());
        content.setType(request.getType());
        content.setCategoryId(request.getCategoryId());
        content.setDescription(request.getDescription());
        content.setFileId(request.getFileId());
        content.setDifficultyId(request.getDifficultyId());
        content.setTagId(request.getTagId());
        content.setPoliticId(request.getPoliticId());
        content.setStatus(request.getStatus());
        save(content);

        // 根据内容类型保存到不同的表
        if (request.getType() == 1) {
            // 文章类型：保存到 Article 表
            articleService.saveByContentId(content.getId(), request.getContent(), request.getAuthor(), request.getSource());
        } else if (request.getType() == 2) {
            // 视频类型：保存到 Video 表
            videoService.saveByContentId(content.getId(), request.getDuration(), request.getCoverId(),
                    request.getResolution(), request.getFormat(), request.getSize());
        }
    }

    @Override
    public void updateBy(ContentSaveRequest request) {
        LambdaUpdateWrapper<Content> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Content::getId, request.getId());
        updateWrapper.set(Objects.nonNull(request.getTitle()), Content::getTitle, request.getTitle());
        updateWrapper.set(Objects.nonNull(request.getDescription()), Content::getDescription, request.getDescription());
        updateWrapper.set(Objects.nonNull(request.getFileId()), Content::getFileId, request.getFileId());
        updateWrapper.set(Objects.nonNull(request.getDifficultyId()), Content::getDifficultyId, request.getDifficultyId());
        updateWrapper.set(Objects.nonNull(request.getTagId()), Content::getTagId, request.getTagId());
        updateWrapper.set(Objects.nonNull(request.getPoliticId()), Content::getPoliticId, request.getPoliticId());
        updateWrapper.set(Objects.nonNull(request.getStatus()), Content::getStatus, request.getStatus());
        updateWrapper.set(Objects.nonNull(request.getCategoryId()), Content::getCategoryId, request.getCategoryId());
        update(updateWrapper);

        // 根据内容类型更新不同的表
        Content existingContent = getById(request.getId());
        if (existingContent != null) {
            if (existingContent.getType() == 1) {
                // 文章类型：更新 Article 表
                articleService.updateByContentId(request.getId(), request.getContent(), request.getAuthor(), request.getSource());
            } else if (existingContent.getType() == 2) {
                // 视频类型：更新 Video 表
                videoService.updateByContentId(request.getId(), request.getDuration(), request.getCoverId(),
                        request.getResolution(), request.getFormat(), request.getSize());
            }
        }
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Content> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Content::getId, id);
        updateWrapper.set(Content::getStatus, status ? (byte) 1 : 0);
        update(updateWrapper);
    }

    @Override
    public boolean removeById(Long id) {
        // 先查询内容类型
        Content content = getById(id);
        if (content != null) {
            // 根据类型删除关联数据
            if (content.getType() == 1) {
                // 文章类型：删除 Article 表中的数据
                articleService.removeByContentId(id);
            } else if (content.getType() == 2) {
                // 视频类型：删除 Video 表中的数据
                videoService.removeByContentId(id);
            }
        }
        // 删除 Content 表中的数据
        return super.removeById(id);
    }

    private List<Content> selectListBy() {
        LambdaQueryWrapper<Content> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Content::getId);
        return baseMapper.selectList(queryWrapper);
    }
    
    private List<Content> selectListByRequestParams(ContentPageRequest request) {
        LambdaQueryWrapper<Content> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据标题查询
        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            queryWrapper.like(Content::getTitle, request.getTitle());
        }
        
        // 根据内容类型查询
        if (request.getType() != null) {
            queryWrapper.eq(Content::getType, request.getType());
        }
        
        // 根据分类ID查询
        if (request.getCategoryId() != null) {
            queryWrapper.eq(Content::getCategoryId, request.getCategoryId());
        }
        
        // 根据难度等级ID查询
        if (request.getDifficultyId() != null) {
            queryWrapper.eq(Content::getDifficultyId, request.getDifficultyId());
        }
        
        // 根据政治面貌ID查询
        if (request.getPoliticId() != null) {
            queryWrapper.eq(Content::getPoliticId, request.getPoliticId());
        }
        
        // 根据状态查询
        if (request.getStatus() != null) {
            queryWrapper.eq(Content::getStatus, request.getStatus());
        }
        
        // 根据排序字段和排序方向进行排序
        if (request.getOrderBy() != null && !request.getOrderBy().trim().isEmpty()) {
            String[] orderFields = request.getOrderBy().split(",");
            if ("DESC".equalsIgnoreCase(request.getOrderType())) {
                queryWrapper.last("ORDER BY " + String.join(" DESC, ", orderFields) + " DESC");
            } else {
                queryWrapper.last("ORDER BY " + String.join(" ASC, ", orderFields) + " ASC");
            }
        } else {
            // 默认按ID降序排列
            queryWrapper.orderByDesc(Content::getId);
        }
        
        return baseMapper.selectList(queryWrapper);
    }
    
    /**
     * 将Content实体列表转换为ContentResponse列表
     */
    private List<ContentResponse> convertToResponseList(List<Content> contents) {
        // 提取所有需要的ID列表
        List<Long> categoryIds = contents.stream()
            .map(Content::getCategoryId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        List<Long> difficultyIds = contents.stream()
            .map(Content::getDifficultyId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        List<Long> fileIds = contents.stream()
            .map(Content::getFileId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        List<Long> politicIds = contents.stream()
            .map(Content::getPoliticId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        List<Long> tagIds = contents.stream()
            .map(Content::getTagId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        // 获取所有内容ID，用于查询文章和视频
        List<Long> contentIds = contents.stream()
            .map(Content::getId)
            .collect(Collectors.toList());

        // 批量查询关联数据
        Map<Long, String> categoryMap = getCategoryMap(categoryIds);
        Map<Long, String> difficultyMap = getDifficultyMap(difficultyIds);
        Map<Long, String> fileMap = getFileMap(fileIds);
        Map<Long, String> politicMap = getPoliticMap(politicIds);
        Map<Long, String> tagMap = getTagMap(tagIds);
        Map<Long, Article> articleMap = getArticleMap(contentIds);
        Map<Long, Video> videoMap = getVideoMap(contentIds);
        Map<Long, String> coverMap = getCoverMap(videoMap.values());

        // 转换为Response对象
        return contents.stream()
            .map(content -> {
                ContentResponse response = new ContentResponse();
                response.setId(content.getId());
                response.setTitle(content.getTitle());
                response.setType(content.getType());
                response.setCategoryId(content.getCategoryId());
                response.setCategoryName(categoryMap.get(content.getCategoryId()));
                response.setDescription(content.getDescription());
                response.setFileId(content.getFileId());
                response.setFileImage(fileMap.get(content.getFileId()));
                response.setDifficultyId(content.getDifficultyId());
                response.setDifficultyName(difficultyMap.get(content.getDifficultyId()));
                response.setTagId(content.getTagId());
                response.setTagName(tagMap.get(content.getTagId()));
                response.setPoliticId(content.getPoliticId());
                response.setPoliticName(politicMap.get(content.getPoliticId()));
                response.setStatus(content.getStatus());
                response.setViewCount(content.getViewCount());
                response.setLikeCount(content.getLikeCount());
                response.setCollectCount(content.getCollectCount());
                response.setCreateTime(content.getCreateTime());
                response.setUpdateTime(content.getUpdateTime());

                // 文章相关字段
                Article article = articleMap.get(content.getId());
                if (article != null) {
                    response.setContent(article.getContent());
                    response.setAuthor(article.getAuthor());
                    response.setSource(article.getSource());
                }

                // 视频相关字段
                Video video = videoMap.get(content.getId());
                if (video != null) {
                    response.setDuration(video.getDuration());
                    response.setCoverId(video.getCoverId());
                    response.setCoverUrl(coverMap.get(video.getCoverId()));
                    response.setResolution(video.getResolution());
                    response.setFormat(video.getFormat());
                    response.setSize(video.getSize());
                }

                return response;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 获取分类名称映射
     */
    private Map<Long, String> getCategoryMap(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Map.of();
        }
        
        List<ContentCategory> categories = contentCategoryService.listByIds(categoryIds);
        return categories.stream()
            .collect(Collectors.toMap(ContentCategory::getId, ContentCategory::getCategoryName));
    }
    
    /**
     * 获取难度名称映射
     */
    private Map<Long, String> getDifficultyMap(List<Long> difficultyIds) {
        if (difficultyIds == null || difficultyIds.isEmpty()) {
            return Map.of();
        }
        
        List<Difficulty> difficulties = difficultyService.listByIds(difficultyIds);
        return difficulties.stream()
            .collect(Collectors.toMap(Difficulty::getId, Difficulty::getName));
    }
    
    /**
     * 获取文件名称映射
     */
    private Map<Long, String> getFileMap(List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) {
            return Map.of();
        }

        List<File> files = fileService.listByIds(fileIds);
        return files.stream()
            .collect(Collectors.toMap(File::getId, File::getFilePathUrl));
    }

    /**
     * 获取封面图URL映射
     */
    private Map<Long, String> getCoverMap(List<Long> coverIds) {
        if (coverIds == null || coverIds.isEmpty()) {
            return Map.of();
        }

        List<File> files = fileService.listByIds(coverIds);
        return files.stream()
            .collect(Collectors.toMap(File::getId, File::getFilePathUrl));
    }

    /**
     * 获取封面图URL映射（从Video对象中提取）
     */
    private Map<Long, String> getCoverMap(java.util.Collection<Video> videos) {
        if (videos == null || videos.isEmpty()) {
            return Map.of();
        }

        List<Long> coverIds = videos.stream()
            .map(Video::getCoverId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        return getCoverMap(coverIds);
    }

    /**
     * 获取文章映射
     */
    private Map<Long, Article> getArticleMap(List<Long> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            return Map.of();
        }

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Article::getContentId, contentIds);
        List<Article> articles = articleService.list(queryWrapper);
        return articles.stream()
            .collect(Collectors.toMap(Article::getContentId, article -> article));
    }

    /**
     * 获取视频映射
     */
    private Map<Long, Video> getVideoMap(List<Long> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            return Map.of();
        }

        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Video::getContentId, contentIds);
        List<Video> videos = videoService.list(queryWrapper);
        return videos.stream()
            .collect(Collectors.toMap(Video::getContentId, video -> video));
    }
    
    /**
     * 获取政治面貌名称映射
     */
    private Map<Long, String> getPoliticMap(List<Long> politicIds) {
        if (politicIds == null || politicIds.isEmpty()) {
            return Map.of();
        }
        
        List<Politic> politics = politicService.listByIds(politicIds);
        return politics.stream()
            .collect(Collectors.toMap(Politic::getId, Politic::getName));
    }
    
    /**
     * 获取标签名称映射
     */
    private Map<Long, String> getTagMap(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Map.of();
        }
        
        List<Tag> tags = tagService.listByIds(tagIds);
        return tags.stream()
            .collect(Collectors.toMap(Tag::getId, Tag::getName));
    }
}