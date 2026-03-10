package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.ContentCategory;
import com.xhonell.common.domain.request.ContentCategoryPageRequest;
import com.xhonell.common.domain.response.ContentCategoryResponse;
import com.xhonell.common.domain.response.TreeSelectOption;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ContentCategoryService
 * description: 内容分类Service
 * author: xhonell
 * create: 2026年3月7日
 * Version 1.0
 **/
public interface ContentCategoryService extends IService<ContentCategory> {

    List<ContentCategoryResponse> selectList(ContentCategoryPageRequest request);

    List<ContentCategoryResponse> getParentCategoryList();

    void saveBy(ContentCategory contentCategory);

    void updateBy(ContentCategory contentCategory);

    void updateStatus(Long id, Boolean status);

    /**
     * 获取启用状态的内容分类下拉列表（树形结构）
     */
    List<TreeSelectOption> selectEnabledList();
}