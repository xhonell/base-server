package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.PeContentMapper;
import com.xhonell.admin.service.PeContentService;
import com.xhonell.common.domain.entity.PeContent;
import com.xhonell.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName PeContentServiceImpl
 * description: 教育内容Service实现
 * author: xhonell
 * create: 2026年1月18日
 * Version 1.0
 **/
@Service
public class PeContentServiceImpl extends ServiceImpl<PeContentMapper, PeContent> implements PeContentService {

    @Override
    public PageInfo<PeContent> selectList(Integer page, Integer pageSize) {
        PageUtils.startPage(page, pageSize);
        List<PeContent> peContents = selectListBy();
        return PageUtils.toPageInfo(peContents);
    }

    @Override
    public void saveBy(PeContent peContent) {
        save(peContent);
    }

    @Override
    public void updateBy(PeContent peContent) {
        LambdaUpdateWrapper<PeContent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PeContent::getId, peContent.getId());
        updateWrapper.set(Objects.nonNull(peContent.getTitle()), PeContent::getTitle, peContent.getTitle());
        updateWrapper.set(Objects.nonNull(peContent.getType()), PeContent::getType, peContent.getType());
        updateWrapper.set(Objects.nonNull(peContent.getDescription()), PeContent::getDescription, peContent.getDescription());
        updateWrapper.set(Objects.nonNull(peContent.getFileId()), PeContent::getFileId, peContent.getFileId());
        updateWrapper.set(Objects.nonNull(peContent.getDifficultyId()), PeContent::getDifficultyId, peContent.getDifficultyId());
        updateWrapper.set(Objects.nonNull(peContent.getTagId()), PeContent::getTagId, peContent.getTagId());
        updateWrapper.set(Objects.nonNull(peContent.getPoliticId()), PeContent::getPoliticId, peContent.getPoliticId());
        updateWrapper.set(Objects.nonNull(peContent.getStatus()), PeContent::getStatus, peContent.getStatus());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<PeContent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PeContent::getId, id);
        updateWrapper.set(PeContent::getStatus, status ? (byte) 1 : 0);
        update(updateWrapper);
    }

    private List<PeContent> selectListBy() {
        LambdaQueryWrapper<PeContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PeContent::getId);
        return baseMapper.selectList(queryWrapper);
    }
}