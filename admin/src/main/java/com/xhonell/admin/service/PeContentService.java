package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.PeContent;

/**
 * program: BaseServer
 * ClassName PeContentService
 * description: 教育内容Service
 * author: xhonell
 * create: 2026年1月18日
 * Version 1.0
 **/
public interface PeContentService extends IService<PeContent> {

    PageInfo<PeContent> selectList(Integer page, Integer pageSize);

    void saveBy(PeContent peContent);

    void updateBy(PeContent peContent);

    void updateStatus(Long id, Boolean status);
}