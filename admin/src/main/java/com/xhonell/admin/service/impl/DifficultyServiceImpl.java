package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.DifficultyMapper;
import com.xhonell.admin.service.DifficultyService;
import com.xhonell.common.domain.entity.Difficulty;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.DifficultyPageRequest;
import com.xhonell.common.domain.request.PoliticPageRequest;
import com.xhonell.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName DifficultyServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月26日21时33分
 * Version 1.0
 **/
@Service
public class DifficultyServiceImpl extends ServiceImpl<DifficultyMapper, Difficulty> implements DifficultyService {
    @Override
    public PageInfo<Difficulty> selectList(DifficultyPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<Difficulty> politics = selectListBy(request);
        return PageUtils.toPageInfo(politics);
    }

    @Override
    public void saveBy(Difficulty difficulty) {
        save( difficulty);
    }

    @Override
    public void updateBy(Difficulty difficulty) {
        LambdaUpdateWrapper<Difficulty> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Difficulty::getId, difficulty.getId());
        updateWrapper.set(Objects.nonNull(difficulty.getDescription()), Difficulty::getDescription, difficulty.getDescription());
        updateWrapper.set(Objects.nonNull(difficulty.getName()), Difficulty::getName, difficulty.getName());
        updateWrapper.set(Objects.nonNull(difficulty.getStatus()), Difficulty::getStatus, difficulty.getStatus());
        updateWrapper.set(Objects.nonNull(difficulty.getScore()), Difficulty::getScore, difficulty.getScore());
        updateWrapper.set(Objects.nonNull(difficulty.getStarts()), Difficulty::getStarts, difficulty.getStarts());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Difficulty> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Difficulty::getId, id);
        updateWrapper.set(Difficulty::getStatus, status);
        update(updateWrapper);
    }

    private List<Difficulty> selectListBy(DifficultyPageRequest request) {
        LambdaQueryWrapper<Difficulty> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Difficulty::getId);
        return baseMapper.selectList(queryWrapper);
    }
}
