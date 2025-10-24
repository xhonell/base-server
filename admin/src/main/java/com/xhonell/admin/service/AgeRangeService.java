package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.AgeRangeMapper;
import com.xhonell.common.domain.entity.AgeRange;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.AgeRangePageRequest;

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
}
