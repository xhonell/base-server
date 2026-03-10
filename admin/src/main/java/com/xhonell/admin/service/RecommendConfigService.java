package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.RecommendConfig;
import com.xhonell.common.domain.request.RecommendConfigPageRequest;
import com.xhonell.common.domain.request.RecommendConfigSaveRequest;
import com.xhonell.common.domain.response.RecommendConfigResponse;

/**
 * program: BaseServer
 * ClassName RecommendConfigService
 * description: 推荐配置Service接口
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
public interface RecommendConfigService extends IService<RecommendConfig> {

    /**
     * 分页查询推荐配置列表
     */
    PageInfo<RecommendConfigResponse> selectListByRequest(RecommendConfigPageRequest request);

    /**
     * 保存推荐配置
     */
    void saveBy(RecommendConfigSaveRequest request);

    /**
     * 更新推荐配置
     */
    void updateBy(RecommendConfigSaveRequest request);

    /**
     * 获取当前生效的推荐配置
     */
    RecommendConfigResponse getActiveConfig();
}