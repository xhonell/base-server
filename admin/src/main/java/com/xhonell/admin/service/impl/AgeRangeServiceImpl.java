package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.AgeRangeMapper;
import com.xhonell.admin.service.AgeRangeService;
import com.xhonell.common.domain.entity.AgeRange;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.AgeRangePageRequest;
import com.xhonell.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName AgeRangeServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月24日21时48分
 * Version 1.0
 **/
@Service
public class AgeRangeServiceImpl extends ServiceImpl<AgeRangeMapper, AgeRange> implements AgeRangeService {
    @Override
    public PageInfo<AgeRange> selectList(AgeRangePageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<AgeRange> ageRanges = selectListBy(request);
        return PageUtils.toPageInfo(ageRanges);
    }

    private List<AgeRange> selectListBy(AgeRangePageRequest request) {
        LambdaQueryWrapper<AgeRange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AgeRange::getId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void saveBy(AgeRange ageRange) {
        save(ageRange);
    }

    @Override
    public void updateBy(AgeRange ageRange) {
        LambdaUpdateWrapper<AgeRange> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AgeRange::getId, ageRange.getId());
        updateWrapper.set(Objects.nonNull(ageRange.getMaxAge()), AgeRange::getMaxAge, ageRange.getMaxAge());
        updateWrapper.set(Objects.nonNull(ageRange.getMinAge()), AgeRange::getMinAge, ageRange.getMinAge());
        update(ageRange, updateWrapper);
    }
}
