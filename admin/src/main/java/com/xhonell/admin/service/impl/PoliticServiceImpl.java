package com.xhonell.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.PoliticMapper;
import com.xhonell.admin.service.PoliticService;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.PoliticPageRequest;
import com.xhonell.common.domain.response.BannerResponse;
import com.xhonell.common.utils.ListUtil;
import com.xhonell.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName PoliticServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月24日21时12分
 * Version 1.0
 **/
@Service
public class PoliticServiceImpl extends ServiceImpl<PoliticMapper, Politic> implements PoliticService {
    @Override
    public PageInfo<Politic> selectList(PoliticPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<Politic> politics = selectListBy(request);
        return PageUtils.toPageInfo(politics);
    }

    @Override
    public void saveBy(Politic politic) {
        save( politic);
    }

    @Override
    public void updateBy(Politic politic) {
        LambdaUpdateWrapper<Politic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Politic::getId, politic.getId());
        updateWrapper.set(Objects.nonNull(politic.getDescription()), Politic::getDescription, politic.getDescription());
        updateWrapper.set(Objects.nonNull(politic.getName()), Politic::getName, politic.getName());
        updateWrapper.set(Objects.nonNull(politic.getStatus()), Politic::getStatus, politic.getStatus());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Politic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Politic::getId, id);
        updateWrapper.set(Politic::getStatus, status);
        update(updateWrapper);
    }

    private List<Politic> selectListBy(PoliticPageRequest request) {
        LambdaQueryWrapper<Politic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Politic::getId);
        return baseMapper.selectList(queryWrapper);
    }
}
