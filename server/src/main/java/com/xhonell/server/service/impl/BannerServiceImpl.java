package com.xhonell.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.request.BannerPageRequest;
import com.xhonell.common.domain.response.BannerResponse;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.ListUtil;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.server.mapper.BannerMapper;
import com.xhonell.server.service.BannerService;
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
    public List<BannerResponse> selectList() {
        List<Banner> banners = selectListBy();
        if (CollectionUtil.isEmpty(banners)) {
            return Collections.emptyList();
        }
        List<BannerResponse> bannerResponses = ListUtil.toList(banners, BannerResponse.class);
        List<Long> fileIds = ListUtil.toList(bannerResponses, BannerResponse::getFileId);
        List<File> files = fileServiceImpl.listByIds(fileIds);
        Map<Long, String> fileMap = ListUtil.toMap(files, File::getId, File::getFilePathUrl);
        ListUtil.toList(bannerResponses, bannerResponse -> bannerResponse.setFilePathUrl(fileMap.get(bannerResponse.getFileId())));
        return bannerResponses;
    }

    private List<Banner> selectListBy() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Banner::getStatus, Boolean.TRUE);
        queryWrapper.orderByDesc(Banner::getId);
        queryWrapper.last("limit 5");
        return baseMapper.selectList(queryWrapper);
    }
}
