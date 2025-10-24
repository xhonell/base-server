package com.xhonell.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.BannerMapper;
import com.xhonell.admin.service.BannerService;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.request.BannerPageRequest;
import com.xhonell.common.domain.response.BannerResponse;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.ListUtil;
import com.xhonell.common.utils.PageUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName BannerServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月19日22时50分
 * Version 1.0
 **/
@Service
@AllArgsConstructor
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    private final FileServiceImpl fileServiceImpl;
    @Override
    public PageInfo<BannerResponse> selectList(BannerPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<Banner> banners = selectListBy(request);
        if (CollectionUtil.isEmpty(banners)) {
            return PageUtils.toPageInfo(Collections.emptyList());
        }
        List<BannerResponse> bannerResponses = ListUtil.toList(banners, BannerResponse.class);
        List<Long> fileIds = ListUtil.toList(bannerResponses, BannerResponse::getFileId);
        List<File> files = fileServiceImpl.listByIds(fileIds);
        Map<Long, String> fileMap = ListUtil.toMap(files, File::getId, File::getFilePathUrl);
        ListUtil.toList(bannerResponses, bannerResponse -> bannerResponse.setFilePathUrl(fileMap.get(bannerResponse.getFileId())));
        return PageUtils.toPageInfo(bannerResponses);
    }

    @Override
    public void saveBy(Banner banner) {
        if (banner.getStatus()) {
            Long total = selectCountBy();
            AssertUtil.isTrue(total < 5, "最多只能添加5个轮播图");
        }
        AssertUtil.isTrue(Objects.nonNull(banner.getFileId()), "请选择图片");
        save( banner);
    }

    @Override
    public void updateBy(Banner banner) {
        if (banner.getStatus()) {
            Long total = selectCountBy();
            AssertUtil.isTrue(total < 5, "最多只能添加5个轮播图");
        }
        LambdaUpdateWrapper<Banner> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Banner::getId, banner.getId());
        updateWrapper.set(Objects.nonNull(banner.getTitle()), Banner::getTitle, banner.getTitle());
        updateWrapper.set(Objects.nonNull(banner.getStatus()), Banner::getStatus, banner.getStatus());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        if (status) {
            Long total = selectCountBy();
            AssertUtil.isTrue(total < 5, "最多只能添加5个轮播图");
        }
        LambdaUpdateWrapper<Banner> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Banner::getId, id);
        updateWrapper.set(Banner::getStatus, status);
        update(updateWrapper);
    }

    private List<Banner> selectListBy(BannerPageRequest request) {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Banner::getId);
        return baseMapper.selectList(queryWrapper);
    }

    private Long selectCountBy() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Banner::getStatus, Boolean.TRUE);
        return baseMapper.selectCount(queryWrapper);
    }
}
