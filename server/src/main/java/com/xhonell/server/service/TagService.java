package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Tag;
import com.xhonell.common.domain.request.TagPageRequest;
import com.xhonell.common.domain.request.TagSaveRequest;
import com.xhonell.common.domain.response.SelectOption;
import com.xhonell.common.domain.response.TagResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName TagService
 * description: 标签Service接口
 * author: xhonell
 * create: 2026年3月8日
 * Version 1.0
 **/
public interface TagService extends IService<Tag> {

    /**
     * 分页查询标签列表
     */
    PageInfo<TagResponse> selectListByRequest(TagPageRequest request);

    /**
     * 保存标签
     */
    void saveBy(TagSaveRequest request);

    /**
     * 更新标签
     */
    void updateBy(TagSaveRequest request);

    /**
     * 更新标签状态
     */
    void updateStatus(Long id, Boolean status);

    /**
     * 获取启用状态的标签下拉列表
     */
    List<SelectOption> selectEnabledList();
}