package com.xhonell.admin.service;

import com.xhonell.common.domain.request.RecommendRequest;
import com.xhonell.common.domain.response.RecommendResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName RecommendService
 * description: 推荐算法服务接口
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
public interface RecommendService {

    /**
     * 根据推荐算法获取推荐内容列表
     *
     * @param request 推荐请求参数
     * @return 推荐内容列表
     */
    List<RecommendResponse> recommend(RecommendRequest request);
}