package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.ContentMapper;
import com.xhonell.admin.service.ContentCategoryService;
import com.xhonell.admin.service.DifficultyService;
import com.xhonell.admin.service.FileService;
import com.xhonell.admin.service.PoliticService;
import com.xhonell.admin.service.TagService;
import com.xhonell.admin.service.ContentService;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.entity.ContentCategory;
import com.xhonell.common.domain.entity.Difficulty;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.entity.Tag;
import com.xhonell.common.domain.request.ContentPageRequest;
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
    public void saveBy(Content peContent) {
        save(peContent);
    }

    @Override
    public void updateBy(Content peContent) {
        LambdaUpdateWrapper<Content> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Content::getId, peContent.getId());
        updateWrapper.set(Objects.nonNull(peContent.getTitle()), Content::getTitle, peContent.getTitle());
        updateWrapper.set(Objects.nonNull(peContent.getType()), Content::getType, peContent.getType());
        updateWrapper.set(Objects.nonNull(peContent.getDescription()), Content::getDescription, peContent.getDescription());
        updateWrapper.set(Objects.nonNull(peContent.getFileId()), Content::getFileId, peContent.getFileId());
        updateWrapper.set(Objects.nonNull(peContent.getDifficultyId()), Content::getDifficultyId, peContent.getDifficultyId());
        updateWrapper.set(Objects.nonNull(peContent.getTagId()), Content::getTagId, peContent.getTagId());
        updateWrapper.set(Objects.nonNull(peContent.getPoliticId()), Content::getPoliticId, peContent.getPoliticId());
        updateWrapper.set(Objects.nonNull(peContent.getStatus()), Content::getStatus, peContent.getStatus());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Content> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Content::getId, id);
        updateWrapper.set(Content::getStatus, status ? (byte) 1 : 0);
        update(updateWrapper);
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

        // 批量查询关联数据
        Map<Long, String> categoryMap = getCategoryMap(categoryIds);
        Map<Long, String> difficultyMap = getDifficultyMap(difficultyIds);
        Map<Long, String> fileMap = getFileMap(fileIds);
        Map<Long, String> politicMap = getPoliticMap(politicIds);
        Map<Long, String> tagMap = getTagMap(tagIds);

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
                response.setFileName(fileMap.get(content.getFileId()));
                response.setDifficultyId(content.getDifficultyId());
                response.setDifficultyName(difficultyMap.get(content.getDifficultyId()));
                response.setTagId(content.getTagId());
                response.setTagName(tagMap.get(content.getTagId()));
                response.setPoliticId(content.getPoliticId());
                response.setPoliticName(politicMap.get(content.getPoliticId()));
                response.setStatus(content.getStatus());
                response.setCreateTime(content.getCreateTime());
                response.setUpdateTime(content.getUpdateTime());
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
            .collect(Collectors.toMap(File::getId, File::getFileUrl));
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