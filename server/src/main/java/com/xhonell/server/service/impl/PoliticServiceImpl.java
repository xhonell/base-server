package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.PoliticPageRequest;
import com.xhonell.common.domain.response.SelectOption;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.server.mapper.PoliticMapper;
import com.xhonell.server.service.PoliticService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public List<SelectOption> selectEnabledList() {
        LambdaQueryWrapper<Politic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Politic::getStatus, (byte) 1);
        queryWrapper.orderByAsc(Politic::getId);
        List<Politic> politics = baseMapper.selectList(queryWrapper);
        return politics.stream()
                .map(politic -> new SelectOption(politic.getId(), politic.getName()))
                .collect(Collectors.toList());
    }

    private List<Politic> selectListBy(PoliticPageRequest request) {
        LambdaQueryWrapper<Politic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Politic::getId);
        return baseMapper.selectList(queryWrapper);
    }
}
