package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.request.ContentPageRequest;
import com.xhonell.common.domain.request.ContentSaveRequest;
import com.xhonell.common.domain.response.ContentResponse;

/**
 * program: BaseServer
 * ClassName PeContentService
 * description: 教育内容Service
 * author: xhonell
 * create: 2026年1月18日
 * Version 1.0
 **/
public interface ContentService extends IService<Content> {

    PageInfo<ContentResponse> selectList(Integer page, Integer pageSize);

    PageInfo<ContentResponse> selectListByRequest(ContentPageRequest request);

    void saveBy(ContentSaveRequest request);

    void updateBy(ContentSaveRequest request);

    void updateStatus(Long id, Boolean status);

    /**
     * 删除内容，同时删除关联的文章或视频
     */
    boolean removeById(Long id);
}