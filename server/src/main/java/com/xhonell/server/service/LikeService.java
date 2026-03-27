package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.LikeRecord;
import com.xhonell.common.domain.request.LikeRequest;

/**
 * program: BaseServer
 * ClassName LikeService
 * description: 点赞服务
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
public interface LikeService extends IService<LikeRecord> {

    /**
     * 处理点赞/取消点赞
     *
     * @param request 点赞请求
     */
    void handleLike(LikeRequest request);

    /**
     * 检查用户是否已点赞
     *
     * @param contentId 内容ID
     * @param userId    用户ID
     * @return 是否已点赞
     */
    Boolean isLiked(Long contentId, Long userId);
}