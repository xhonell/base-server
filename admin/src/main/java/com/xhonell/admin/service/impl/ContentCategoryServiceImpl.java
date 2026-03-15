package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.ContentCategoryMapper;
import com.xhonell.admin.service.ContentCategoryService;
import com.xhonell.common.domain.entity.ContentCategory;
import com.xhonell.common.domain.request.ContentCategoryPageRequest;
import com.xhonell.common.domain.response.ContentCategoryResponse;
import com.xhonell.common.domain.response.TreeSelectOption;
import com.xhonell.common.utils.TreeBuilderUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName ContentCategoryServiceImpl
 * description: 内容分类Service实现
 * author: xhonell
 * create: 2026年3月7日
 * Version 1.0
 **/
@Service
public class ContentCategoryServiceImpl extends ServiceImpl<ContentCategoryMapper, ContentCategory> implements ContentCategoryService {

    @Override
    public List<ContentCategoryResponse> selectList(ContentCategoryPageRequest request) {
        List<ContentCategory> contentCategories = selectListBy(request);
        // 将实体类转换为响应类
        List<ContentCategoryResponse> responses = contentCategories.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        // 使用TreeBuilderUtil构建树状结构
        return TreeBuilderUtil.buildTree(
            responses,
            ContentCategoryResponse::getId,
            ContentCategoryResponse::getParentId,
            (node, children) -> node.setChildren(children)
        );
    }

    @Override
    public List<ContentCategoryResponse> getParentCategoryList() {
        // 查询所有父级分类（parentId为null或0的分类）
        LambdaQueryWrapper<ContentCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentCategory::getParentId, 0L).or()
                   .isNull(ContentCategory::getParentId);
        queryWrapper.orderByDesc(ContentCategory::getId);

        List<ContentCategory> contentCategories = baseMapper.selectList(queryWrapper);
        // 将实体类转换为响应类
        return contentCategories.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void saveBy(ContentCategory contentCategory) {
        save(contentCategory);
    }

    @Override
    public void updateBy(ContentCategory contentCategory) {
        LambdaUpdateWrapper<ContentCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContentCategory::getId, contentCategory.getId());
        updateWrapper.set(Objects.nonNull(contentCategory.getIcon()), ContentCategory::getIcon, contentCategory.getIcon());
        updateWrapper.set(Objects.nonNull(contentCategory.getCategoryName()), ContentCategory::getCategoryName, contentCategory.getCategoryName());
        updateWrapper.set(Objects.nonNull(contentCategory.getDescription()), ContentCategory::getDescription, contentCategory.getDescription());
        updateWrapper.set(Objects.nonNull(contentCategory.getWeight()), ContentCategory::getWeight, contentCategory.getWeight());
        updateWrapper.set(Objects.nonNull(contentCategory.getParentId()), ContentCategory::getParentId, contentCategory.getParentId());
        updateWrapper.set(Objects.nonNull(contentCategory.getStatus()), ContentCategory::getStatus, contentCategory.getStatus());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<ContentCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContentCategory::getId, id);
        updateWrapper.set(ContentCategory::getStatus, status ? (byte) 1 : 0);
        update(updateWrapper);
    }

    @Override
    public List<TreeSelectOption> selectEnabledList() {
        LambdaQueryWrapper<ContentCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentCategory::getStatus, 1L);
        queryWrapper.orderByAsc(ContentCategory::getId);
        List<ContentCategory> contentCategories = baseMapper.selectList(queryWrapper);

        // 转换为树形下拉选项
        List<TreeSelectOption> options = contentCategories.stream()
                .map(category -> new TreeSelectOption(category.getId(), category.getCategoryName(), null))
                .collect(Collectors.toList());

        // 使用TreeBuilderUtil构建树状结构
        return TreeBuilderUtil.buildTree(
            options,
            TreeSelectOption::getValue,
            (node) -> {
                // 查找父ID，通过原始列表查找
                ContentCategory category = contentCategories.stream()
                    .filter(c -> c.getId().equals(node.getValue()))
                    .findFirst()
                    .orElse(null);
                return category != null ? category.getParentId() : null;
            },
                TreeSelectOption::setChildren
        );
    }

    private List<ContentCategory> selectListBy(ContentCategoryPageRequest request) {
        LambdaQueryWrapper<ContentCategory> queryWrapper = new LambdaQueryWrapper<>();

        // Apply ordering from request parameters
        queryWrapper.orderByDesc(ContentCategory::getId);

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 将ContentCategory实体转换为ContentCategoryResponse
     */
    private ContentCategoryResponse convertToResponse(ContentCategory entity) {
        return new ContentCategoryResponse()
            .setId(entity.getId())
            .setIcon(entity.getIcon())
            .setCategoryName(entity.getCategoryName())
            .setDescription(entity.getDescription())
            .setWeight(entity.getWeight())
            .setStatus(entity.getStatus())
            .setParentId(entity.getParentId())
            .setCreateTime(entity.getCreateTime())
            .setUpdateTime(entity.getUpdateTime());
    }

}