package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.entity.Video;
import com.xhonell.common.domain.request.VideoPageRequest;
import com.xhonell.common.domain.response.RecommendResponse;
import com.xhonell.common.domain.response.VideoPageResponse;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.server.mapper.VideoMapper;
import com.xhonell.server.service.ContentCategoryService;
import com.xhonell.server.service.ContentService;
import com.xhonell.server.service.FileService;
import com.xhonell.server.service.TagService;
import com.xhonell.server.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * program: BaseServer
 * ClassName VideoServiceImpl
 * description: 视频服务实现 - server模块
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    private final VideoMapper videoMapper;
    private final ContentService contentService;
    private final ContentCategoryService contentCategoryService;
    private final FileService fileService;
    private final TagService tagService;
    private final com.xhonell.server.service.DifficultyService difficultyService;
    private final com.xhonell.server.service.LikeService likeService;
    private final com.xhonell.server.service.CollectService collectService;
    private final com.xhonell.server.mapper.ViewRecordMapper viewRecordMapper;

    @Override
    public VideoPageResponse pageVideo(VideoPageRequest request) {
        // 开始分页
        PageUtils.startPage(request.getPage(), request.getPageSize());

        // 构建查询条件 - 首先查询视频内容
        LambdaQueryWrapper<Content> contentQueryWrapper = new LambdaQueryWrapper<>();
        contentQueryWrapper.eq(Content::getType, (byte) 2) // 视频类型
                .eq(Content::getStatus, 1); // 启用状态

        // 分类ID筛选
        if (request.getCategoryId() != null) {
            contentQueryWrapper.eq(Content::getCategoryId, request.getCategoryId());
        }

        // 排序
        switch (request.getSortBy()) {
            case 1: // 最新发布
                contentQueryWrapper.orderByDesc(Content::getCreateTime);
                break;
            case 2: // 最多点赞
                contentQueryWrapper.orderByDesc(Content::getLikeCount);
                break;
            case 3: // 最多观看
                contentQueryWrapper.orderByDesc(Content::getViewCount);
                break;
            default:
                contentQueryWrapper.orderByDesc(Content::getCreateTime);
                break;
        }

        // 执行查询
        List<Content> contents = contentService.list(contentQueryWrapper);

        // 根据时长范围进一步过滤
        if (request.getMinDuration() != null || request.getMaxDuration() != null) {
            contents = contents.stream()
                    .filter(content -> {
                        // 查询对应的视频记录以获取时长信息
                        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
                        videoQuery.eq(Video::getContentId, content.getId());
                        Video video = videoMapper.selectOne(videoQuery);
                        
                        if (video == null) {
                            return false; // 如果没有视频记录，过滤掉
                        }
                        
                        Long duration = video.getDuration();
                        if (duration == null) {
                            return false; // 如果时长为null，过滤掉
                        }
                        
                        // 检查时长范围
                        boolean minCheck = request.getMinDuration() == null || duration >= request.getMinDuration();
                        boolean maxCheck = request.getMaxDuration() == null || duration <= request.getMaxDuration();
                        
                        return minCheck && maxCheck;
                    })
                    .toList();
        }

        // 转换为RecommendResponse
        List<RecommendResponse> videos = contents.stream()
                .map(this::convertToRecommendResponse)
                .toList();

        // 由于我们对结果进行了过滤，需要重新计算分页信息
        int total = contents.size();
        int start = (request.getPage() - 1) * request.getPageSize();
        int end = Math.min(start + request.getPageSize(), total);
        
        List<RecommendResponse> pagedVideos = videos.subList(start, end);
        
        // 构建返回结果
        return new VideoPageResponse(
                request.getPage(),
                request.getPageSize(),
                (long) total,
                (int) Math.ceil((double) total / request.getPageSize()),
                pagedVideos
        );
    }

    @Override
    public RecommendResponse getVideoDetail(Long id) {
        Content content = contentService.getById(id);
        if (content == null || content.getType() != 2 || content.getStatus() != 1) {
            return null; // 或者抛出异常
        }

        // 增加观看数
        LambdaUpdateWrapper<Content> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Content::getId, id);
        updateWrapper.set(Content::getViewCount, content.getViewCount() == null ? 1 : content.getViewCount() + 1);
        contentService.update(updateWrapper);

        // 添加观看记录
        try {
            Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
            com.xhonell.common.domain.entity.ViewRecord viewRecord = new com.xhonell.common.domain.entity.ViewRecord();
            viewRecord.setUserId(userId);
            viewRecord.setContentId(id);
            viewRecord.setType((byte) 2); // 视频类型
            viewRecord.setCreateTime(java.time.LocalDateTime.now());
            viewRecordMapper.insert(viewRecord);
        } catch (Exception e) {
            // 用户未登录时忽略观看记录
        }

        RecommendResponse response = convertToRecommendResponse(content);

        // 检查是否已点赞
        try {
            Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
            Boolean isLiked = likeService.isLiked(id, userId);
            response.setIsLiked(isLiked);
        } catch (Exception e) {
            // 如果用户未登录，设置为未点赞
            response.setIsLiked(false);
        }

        // 检查是否已收藏
        try {
            Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
            Boolean isCollected = collectService.isCollected(id, userId);
            response.setIsCollected(isCollected);
        } catch (Exception e) {
            // 如果用户未登录，设置为未收藏
            response.setIsCollected(false);
        }

        return response;
    }

    @Override
    public void saveByContentId(Long contentId, Long duration, Long coverId, String resolution, String format, Long size) {
        Video video = new Video();
        video.setContentId(contentId);
        video.setDuration(duration);
        video.setCoverId(coverId);
        video.setResolution(resolution);
        video.setFormat(format);
        video.setSize(size);
        videoMapper.insert(video);
    }

    @Override
    public void updateByContentId(Long contentId, Long duration, Long coverId, String resolution, String format, Long size) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getContentId, contentId);
        Video video = videoMapper.selectOne(queryWrapper);
        
        if (video != null) {
            video.setDuration(duration);
            video.setCoverId(coverId);
            video.setResolution(resolution);
            video.setFormat(format);
            video.setSize(size);
            videoMapper.updateById(video);
        }
    }

    @Override
    public void removeByContentId(Long contentId) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getContentId, contentId);
        videoMapper.delete(queryWrapper);
    }

    @Override
    public boolean save(Video entity) {
        return videoMapper.insert(entity) > 0;
    }

    @Override
    public boolean updateById(Video entity) {
        return videoMapper.updateById(entity) > 0;
    }

    /**
     * 将Content实体转换为RecommendResponse
     */
    private RecommendResponse convertToRecommendResponse(Content content) {
        RecommendResponse response = new RecommendResponse();
        response.setId(content.getId());
        response.setTitle(content.getTitle());
        response.setType(content.getType());
        response.setCategoryId(content.getCategoryId());
        response.setDescription(content.getDescription());
        response.setFileId(content.getFileId());
        response.setViewCount(content.getViewCount());
        response.setLikeCount(content.getLikeCount());
        response.setCollectCount(content.getCollectCount());
        response.setCreateTime(content.getCreateTime());
        response.setTagId(content.getTagId());

        // 设置文件URL
        if (content.getFileId() != null) {
            com.xhonell.common.domain.entity.File file = fileService.getById(content.getFileId());
            if (file != null) {
                response.setFileUrl(file.getFilePathUrl());
            }
        }

        // 从标签服务获取标签名称
        if (content.getTagId() != null) {
            com.xhonell.common.domain.entity.Tag tag = tagService.getById(content.getTagId());
            if (tag != null) {
                response.setTagName(tag.getName());
            }
        }

        // 从难度服务获取难度信息
        if (content.getDifficultyId() != null) {
            com.xhonell.common.domain.entity.Difficulty difficulty = difficultyService.getById(content.getDifficultyId());
            if (difficulty != null) {
                response.setDifficultyName(difficulty.getName());
                response.setDifficultyScore(difficulty.getScore());
            }
        }

        // 丰富分类信息
        if (content.getCategoryId() != null) {
            com.xhonell.common.domain.entity.ContentCategory category = contentCategoryService.getById(content.getCategoryId());
            if (category != null) {
                response.setCategoryName(category.getCategoryName());
            }
        }

        // 丰富视频信息
        enrichWithVideoInfo(response, content.getId());

        return response;
    }

    /**
     * 丰富视频信息
     */
    private void enrichWithVideoInfo(RecommendResponse response, Long contentId) {
        // 查询视频信息
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getContentId, contentId);
        Video video = videoMapper.selectOne(videoQuery);

        if (video != null) {
            response.setDuration(video.getDuration());
            response.setCoverId(video.getCoverId());

            // 获取封面图URL
            if (video.getCoverId() != null) {
                com.xhonell.common.domain.entity.File coverFile = fileService.getById(video.getCoverId());
                if (coverFile != null) {
                    response.setCoverUrl(coverFile.getFilePathUrl());
                }
            }
        }
    }
}