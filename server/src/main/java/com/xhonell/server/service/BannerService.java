package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.response.BannerResponse;

import java.util.List;

public interface BannerService extends IService<Banner> {
    List<BannerResponse> selectList();
}
