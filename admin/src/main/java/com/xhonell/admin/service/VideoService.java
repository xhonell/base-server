package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.Video;

/**
 * program: BaseServer
 * ClassName VideoService
 * description: 视频Service接口
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
public interface VideoService extends IService<Video> {

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