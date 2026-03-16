package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.AgeRange;
import com.xhonell.common.domain.request.AgeRangePageRequest;
import com.xhonell.common.domain.response.SelectOption;

import java.util.List;

/**
 * program: BaseServer
 * ClassName AgeRangeService
 * description:
 * author: xhonell
 * create: 2025年10月24日21时47分
 * Version 1.0
 **/
public interface AgeRangeService extends IService<AgeRange> {
    PageInfo<AgeRange> selectList(AgeRangePageRequest request);

    void saveBy(AgeRange ageRange);

    void updateBy(AgeRange ageRange);

    /**
     * 获取年龄段下拉列表
     */
    List<SelectOption> selectEnabledList();
}
