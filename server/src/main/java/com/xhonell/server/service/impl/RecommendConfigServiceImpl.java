package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.RecommendConfig;
import com.xhonell.server.mapper.RecommendConfigMapper;
import com.xhonell.server.service.RecommendConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName RecommendConfigServiceImpl
 * description: 推荐配置服务实现
 * author: xhonell
 * create: 2026年3月15日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RecommendConfigServiceImpl extends ServiceImpl<RecommendConfigMapper, RecommendConfig> implements RecommendConfigService {
}