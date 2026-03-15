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
import com.xhonell.common.domain.request.DifficultySaveRequest;
import com.xhonell.common.domain.request.PoliticPageRequest;
import com.xhonell.common.domain.response.SelectOption;
import com.xhonell.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public void saveBy(DifficultySaveRequest request) {
        Difficulty difficulty = new Difficulty();
        difficulty.setName(request.getName());
        difficulty.setDescription(request.getDescription());
        difficulty.setScore(request.getScore());
        difficulty.setStarts(request.getStarts());
        difficulty.setStatus(request.getStatus());
        save(difficulty);
    }

    @Override
    public void updateBy(DifficultySaveRequest request) {
        LambdaUpdateWrapper<Difficulty> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Difficulty::getId, request.getId());
        updateWrapper.set(Objects.nonNull(request.getDescription()), Difficulty::getDescription, request.getDescription());
        updateWrapper.set(Objects.nonNull(request.getName()), Difficulty::getName, request.getName());
        updateWrapper.set(Objects.nonNull(request.getStatus()), Difficulty::getStatus, request.getStatus());
        updateWrapper.set(Objects.nonNull(request.getScore()), Difficulty::getScore, request.getScore());
        updateWrapper.set(Objects.nonNull(request.getStarts()), Difficulty::getStarts, request.getStarts());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Difficulty> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Difficulty::getId, id);
        updateWrapper.set(Difficulty::getStatus, status);
        update(updateWrapper);
    }

    @Override
    public List<SelectOption> selectEnabledList() {
        LambdaQueryWrapper<Difficulty> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Difficulty::getStatus, (byte) 1);
        queryWrapper.orderByAsc(Difficulty::getId);
        List<Difficulty> difficulties = baseMapper.selectList(queryWrapper);
        return difficulties.stream()
                .map(difficulty -> new SelectOption(difficulty.getId(), difficulty.getName()))
                .collect(Collectors.toList());
    }

    private List<Difficulty> selectListBy(DifficultyPageRequest request) {
        LambdaQueryWrapper<Difficulty> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Difficulty::getId);
        return baseMapper.selectList(queryWrapper);
    }
}
