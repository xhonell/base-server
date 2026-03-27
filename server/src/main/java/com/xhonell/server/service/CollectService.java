package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.CollectRecord;
import com.xhonell.common.domain.request.CollectRequest;

/**
 * program: BaseServer
 * ClassName CollectService
 * description: 收藏服务
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
public interface CollectService extends IService<CollectRecord> {

    /**
     * 处理收藏/取消收藏
     *
     * @param request 收藏请求
     */
    void handleCollect(CollectRequest request);

    /**
     * 检查用户是否已收藏
     *
     * @param contentId 内容ID
     * @param userId    用户ID
     * @return 是否已收藏
     */
    Boolean isCollected(Long contentId, Long userId);
}