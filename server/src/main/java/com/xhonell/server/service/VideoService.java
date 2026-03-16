package com.xhonell.server.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.Video;
import com.xhonell.common.domain.request.VideoPageRequest;
import com.xhonell.common.domain.response.RecommendResponse;
import com.xhonell.common.domain.response.VideoPageResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName VideoService
 * description: 视频服务接口
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
public interface VideoService extends IService<Video> {

    /**
     * 视频分页查询
     * @param request 查询请求
     * @return 分页结果
     */
    VideoPageResponse pageVideo(VideoPageRequest request);

    /**
     * 获取视频详情
     * @param id 视频ID
     * @return 视频详情
     */
    RecommendResponse getVideoDetail(Long id);

    /**
     * 根据内容ID保存视频
     */
    void saveByContentId(Long contentId, Long duration, Long coverId, String resolution, String format, Long size);

    /**
     * 根据内容ID更新视频
     */
    void updateByContentId(Long contentId, Long duration, Long coverId, String resolution, String format, Long size);

    /**
     * 根据内容ID删除视频
     */
    void removeByContentId(Long contentId);
}