package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.response.StatisticsResponse;
import com.xhonell.server.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName StatisticsServiceImpl
 * description: 统计服务实现
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ContentService contentService;
    private final ArticleService articleService;
    private final VideoService videoService;
    private final UserService userService;

    @Override
    public StatisticsResponse getStatistics() {
        // 统计文章数量（内容类型为1）
        LambdaQueryWrapper<Content> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Content::getType, (byte) 1).eq(Content::getStatus, 1); // 只统计启用的文章
        Long articleCount = contentService.count(articleQuery);

        // 统计视频数量（内容类型为2）
        LambdaQueryWrapper<Content> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Content::getType, (byte) 2).eq(Content::getStatus, 1); // 只统计启用的视频
        Long videoCount = contentService.count(videoQuery);

        // 统计用户数量
        Long userCount = userService.count();

        // 统计内容总数（文章+视频）
        LambdaQueryWrapper<Content> contentQuery = new LambdaQueryWrapper<>();
        contentQuery.eq(Content::getStatus, 1); // 只统计启用的内容
        Long contentCount = contentService.count(contentQuery);

        return new StatisticsResponse(articleCount, userCount, videoCount, contentCount);
    }
}