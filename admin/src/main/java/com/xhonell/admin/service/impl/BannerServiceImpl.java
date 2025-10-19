package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.BannerMapper;
import com.xhonell.admin.service.BannerService;
import com.xhonell.common.domain.entity.Banner;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName BannerServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月19日22时50分
 * Version 1.0
 **/
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
}
