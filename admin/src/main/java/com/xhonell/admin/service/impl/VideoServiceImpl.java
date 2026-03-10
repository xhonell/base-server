package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.VideoMapper;
import com.xhonell.admin.service.VideoService;
import com.xhonell.common.domain.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName VideoServiceImpl
 * description: 视频Service实现
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Override
    public void saveByContentId(Long contentId, Long duration, Long coverId, String resolution, String format, Long size) {
        Video video = new Video();
        video.setContentId(contentId);
        video.setDuration(duration);
        video.setCoverId(coverId);
        video.setResolution(resolution);
        video.setFormat(format);
        video.setSize(size);
        save(video);
    }

    @Override
    public void updateByContentId(Long contentId, Long duration, Long coverId, String resolution, String format, Long size) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getContentId, contentId);
        Video video = getOne(queryWrapper);

        if (video != null) {
            LambdaUpdateWrapper<Video> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Video::getContentId, contentId);
            if (duration != null) {
                updateWrapper.set(Video::getDuration, duration);
            }
            if (coverId != null) {
                updateWrapper.set(Video::getCoverId, coverId);
            }
            if (resolution != null) {
                updateWrapper.set(Video::getResolution, resolution);
            }
            if (format != null) {
                updateWrapper.set(Video::getFormat, format);
            }
            if (size != null) {
                updateWrapper.set(Video::getSize, size);
            }
            update(updateWrapper);
        }
    }

    @Override
    public void removeByContentId(Long contentId) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getContentId, contentId);
        remove(queryWrapper);
    }
}