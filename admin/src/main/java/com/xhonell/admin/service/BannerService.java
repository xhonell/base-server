package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.request.BannerPageRequest;
import com.xhonell.common.domain.response.BannerResponse;

public interface BannerService extends IService<Banner> {
    PageInfo<BannerResponse> selectList(BannerPageRequest request);

    void saveBy(Banner banner);

    void updateBy(Banner banner);

    void updateStatus(Long id, Boolean status);
}
