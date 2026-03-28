package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhonell.admin.mapper.CollectRecordMapper;
import com.xhonell.admin.mapper.LikeRecordMapper;
import com.xhonell.admin.mapper.ViewRecordMapper;
import com.xhonell.admin.service.ArticleService;
import com.xhonell.admin.service.ContentCategoryService;
import com.xhonell.admin.service.ContentService;
import com.xhonell.admin.service.FileService;
import com.xhonell.admin.service.RecommendConfigService;
import com.xhonell.admin.service.RecommendService;
import com.xhonell.admin.service.VideoService;
import com.xhonell.common.domain.entity.Article;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.entity.ContentCategory;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.RecommendConfig;
import com.xhonell.common.domain.entity.Video;
import com.xhonell.common.domain.entity.LikeRecord;
import com.xhonell.common.domain.entity.CollectRecord;
import com.xhonell.common.domain.entity.ViewRecord;
import com.xhonell.common.domain.request.RecommendRequest;
import com.xhonell.common.domain.response.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName RecommendServiceImpl
 * description: 推荐算法服务实现
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendConfigService recommendConfigService;
    private final ContentService contentService;
    private final ArticleService articleService;
    private final VideoService videoService;
    private final ContentCategoryService contentCategoryService;
    private final FileService fileService;
    private final LikeRecordMapper likeRecordMapper;
    private final CollectRecordMapper collectRecordMapper;
    private final ViewRecordMapper viewRecordMapper;

    @Override
    public List<RecommendResponse> recommend(RecommendRequest request) {
        // 获取当前生效的推荐配置
        RecommendConfig config = recommendConfigService.lambdaQuery()
                .eq(RecommendConfig::getStatus, 1)
                .one();

        if (config == null) {
            // 如果没有启用配置，返回默认推荐（热门推荐）
            config = new RecommendConfig();
            config.setAlgorithmType(1);
            config.setRecommendCount(10);
            config.setDiversityWeight(50);
            config.setFreshnessWeight(50);
            config.setHotWeight(50);
        }

        // 确定推荐数量
        Integer count = config.getRecommendCount();
        if (count == null || count <= 0) {
            count = 10;
        }

        // 根据不同的算法类型进行推荐
        List<RecommendResponse> recommendations = switch (config.getAlgorithmType()) {
            case 1 -> recommendByHot(request);
            case 2 -> recommendByFresh(request);
            case 3 -> recommendByMixed(request, config);
            case 4 -> recommendByCollaborative(request);
            default -> recommendByHot(request);
        };

        // 应用多样性过滤
        recommendations = applyDiversityFilter(recommendations, config);

        return recommendations.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * 热门推荐算法
     * 根据阅读量、点赞数、收藏数计算热度分数
     */
    private List<RecommendResponse> recommendByHot(RecommendRequest request) {
        return executeRecommendation(request, this::calculateHotScore, "热门推荐", true);
    }

    /**
     * 最新推荐算法
     * 根据创建时间排序
     */
    private List<RecommendResponse> recommendByFresh(RecommendRequest request) {
        return executeRecommendation(request, this::calculateFreshnessScore, "最新推荐", false);
    }

    /**
     * 混合推荐算法
     * 结合热度、新鲜度和多样性权重
     */
    private List<RecommendResponse> recommendByMixed(RecommendRequest request, RecommendConfig config) {
        return executeRecommendation(request, content -> {
            double hotScore = calculateHotScore(content);
            double freshnessScore = calculateFreshnessScore(content);
            return (hotScore * config.getHotWeight() + freshnessScore * config.getFreshnessWeight()) / 100.0;
        }, "智能推荐", true);
    }

    /**
     * 协同过滤推荐算法
     * 基于物品相似度推荐（基于内容的协同过滤）
     */
    private List<RecommendResponse> recommendByCollaborative(RecommendRequest request) {
        LambdaQueryWrapper<Content> queryWrapper = buildBaseQuery(request);

        // 查询所有内容
        List<Content> contents = contentService.list(queryWrapper);

        if (contents.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算每个内容的特征向量（基于点赞、收藏、查看）
        Map<Long, ContentFeature> contentFeatures = contents.stream()
                .collect(Collectors.toMap(Content::getId, this::calculateContentFeature));

        // 如果有用户ID，使用基于用户历史的协同过滤
        if (request.getUserId() != null) {
            return recommendByUserHistory(request, contents, contentFeatures);
        } else {
            // 没有用户ID，使用基于内容的协同过滤
            return recommendByContentSimilarity(request, contents, contentFeatures);
        }
    }

    /**
     * 基于用户历史行为的协同过滤
     * 使用用户实际点赞、收藏、查看的内容进行推荐
     */
    private List<RecommendResponse> recommendByUserHistory(
            RecommendRequest request,
            List<Content> contents,
            Map<Long, ContentFeature> contentFeatures) {

        // 查询用户点赞的内容
        LambdaQueryWrapper<LikeRecord> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(LikeRecord::getUserId, request.getUserId());
        likeWrapper.eq(LikeRecord::getStatus, 1);
        List<LikeRecord> likeRecords = likeRecordMapper.selectList(likeWrapper);

        // 查询用户收藏的内容
        LambdaQueryWrapper<CollectRecord> collectWrapper = new LambdaQueryWrapper<>();
        collectWrapper.eq(CollectRecord::getUserId, request.getUserId());
        collectWrapper.eq(CollectRecord::getStatus, 1);
        List<CollectRecord> collectRecords = collectRecordMapper.selectList(collectWrapper);

        // 查询用户查看的内容
        LambdaQueryWrapper<ViewRecord> viewWrapper = new LambdaQueryWrapper<>();
        viewWrapper.eq(ViewRecord::getUserId, request.getUserId());
        viewWrapper.orderByDesc(ViewRecord::getCreateTime);
        viewWrapper.last("LIMIT 50"); // 限制查询最近50条查看记录
        List<ViewRecord> viewRecords = viewRecordMapper.selectList(viewWrapper);

        // 获取用户喜欢的内容ID集合（点赞 > 收藏 > 查看）
        Map<Long, Double> likedContents = new HashMap<>();
        
        // 点赞的内容权重最高
        likeRecords.forEach(record -> {
            likedContents.put(record.getContentId(), likedContents.getOrDefault(record.getContentId(), 0.0) + 3.0);
        });
        
        // 收藏的内容权重次之
        collectRecords.forEach(record -> {
            likedContents.put(record.getContentId(), likedContents.getOrDefault(record.getContentId(), 0.0) + 2.0);
        });
        
        // 查看的内容权重最低
        viewRecords.forEach(record -> {
            likedContents.put(record.getContentId(), likedContents.getOrDefault(record.getContentId(), 0.0) + 1.0);
        });

        // 如果用户没有任何行为记录，使用基于热门内容的推荐
        if (likedContents.isEmpty()) {
            return recommendByContentSimilarity(request, contents, contentFeatures);
        }

        // 过滤出用户喜欢的内容实体
        List<Content> likedContentEntities = contents.stream()
                .filter(content -> likedContents.containsKey(content.getId()))
                .toList();

        if (likedContentEntities.isEmpty()) {
            return recommendByContentSimilarity(request, contents, contentFeatures);
        }

        // 计算每个内容与用户喜欢内容的相似度
        Map<Long, Double> contentSimilarity = new HashMap<>();
        for (Content content : contents) {
            if (likedContents.containsKey(content.getId())) {
                continue; // 跳过用户已经喜欢的内容
            }

            double weightedSimilarity = 0.0;
            for (Content likedContent : likedContentEntities) {
                double similarity = calculateSimilarity(
                        contentFeatures.get(content.getId()),
                        contentFeatures.get(likedContent.getId())
                );
                // 根据用户对该内容的喜欢程度加权
                double weight = likedContents.get(likedContent.getId());
                weightedSimilarity += similarity * weight;
            }
            contentSimilarity.put(content.getId(), weightedSimilarity);
        }

        // 转换为响应对象并设置得分
        // 综合得分：相似度权重 + 热度权重

        return contents.stream()
                .map(content -> {
                    RecommendResponse response = convertToResponse(content);
                    double similarity = contentSimilarity.getOrDefault(content.getId(), 0.0);
                    double hotScore = calculateHotScore(content);
                    // 综合得分：相似度权重 + 热度权重
                    response.setScore(similarity * 0.01 + hotScore * 0.001);
                    response.setReason("基于您的历史行为推荐");
                    return response;
                })
                .sorted(Comparator.comparing(RecommendResponse::getScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 基于内容相似度的协同过滤
     * 推荐与其他热门内容相似的内容
     */
    private List<RecommendResponse> recommendByContentSimilarity(
            RecommendRequest request,
            List<Content> contents,
            Map<Long, ContentFeature> contentFeatures) {

        // 找出热门内容
        List<Content> hotContents = contents.stream()
                .sorted(Comparator.comparing(this::calculateHotScore).reversed())
                .limit(10)
                .collect(Collectors.toList());

        if (hotContents.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算每个内容与热门内容的相似度
        Map<Long, Double> contentSimilarity = new HashMap<>();
        for (Content content : contents) {
            if (hotContents.contains(content)) {
                continue; // 跳过热门内容本身
            }

            double maxSimilarity = 0.0;
            for (Content hotContent : hotContents) {
                double similarity = calculateSimilarity(
                        contentFeatures.get(content.getId()),
                        contentFeatures.get(hotContent.getId())
                );
                maxSimilarity = Math.max(maxSimilarity, similarity);
            }
            contentSimilarity.put(content.getId(), maxSimilarity);
        }

        // 转换为响应对象并设置得分
        List<RecommendResponse> responses = contents.stream()
                .map(content -> {
                    RecommendResponse response = convertToResponse(content);
                    double similarity = contentSimilarity.getOrDefault(content.getId(), 0.0);
                    double hotScore = calculateHotScore(content);
                    // 综合得分：相似度 * 0.6 + 热度 * 0.4
                    response.setScore(similarity * 0.6 + hotScore * 0.001);
                    response.setReason("与热门内容相似");
                    return response;
                })
                .sorted(Comparator.comparing(RecommendResponse::getScore).reversed())
                .collect(Collectors.toList());

        return responses;
    }

    /**
     * 计算内容特征
     * 基于点赞、收藏、查看数构建特征向量
     */
    private ContentFeature calculateContentFeature(Content content) {
        int viewCount = content.getViewCount() != null ? content.getViewCount() : 0;
        int likeCount = content.getLikeCount() != null ? content.getLikeCount() : 0;
        int collectCount = content.getCollectCount() != null ? content.getCollectCount() : 0;

        // 归一化特征向量
        double total = viewCount + likeCount + collectCount + 1.0; // +1 避免除以0
        double normalizedView = viewCount / total;
        double normalizedLike = likeCount / total;
        double normalizedCollect = collectCount / total;

        return new ContentFeature(normalizedView, normalizedLike, normalizedCollect);
    }

    /**
     * 计算两个内容特征的相似度（余弦相似度）
     */
    private double calculateSimilarity(ContentFeature f1, ContentFeature f2) {
        if (f1 == null || f2 == null) {
            return 0.0;
        }

        // 计算点积
        double dotProduct = f1.view * f2.view + f1.like * f2.like + f1.collect * f2.collect;

        // 计算向量长度
        double magnitude1 = Math.sqrt(f1.view * f1.view + f1.like * f1.like + f1.collect * f1.collect);
        double magnitude2 = Math.sqrt(f2.view * f2.view + f2.like * f2.like + f2.collect * f2.collect);

        // 避免除以0
        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        // 余弦相似度
        return dotProduct / (magnitude1 * magnitude2);
    }

    /**
     * 内容特征内部类
     */
    private static class ContentFeature {
        double view;    // 归一化的查看数
        double like;    // 归一化的点赞数
        double collect; // 归一化的收藏数

        ContentFeature(double view, double like, double collect) {
            this.view = view;
            this.like = like;
            this.collect = collect;
        }
    }

    /**
     * 执行推荐通用流程
     * @param request 推荐请求
     * @param scoreCalculator 得分计算函数
     * @param reason 推荐原因
     * @param needSort 是否需要按得分排序
     */
    private List<RecommendResponse> executeRecommendation(
            RecommendRequest request,
            java.util.function.Function<Content, Double> scoreCalculator,
            String reason,
            boolean needSort) {
        LambdaQueryWrapper<Content> queryWrapper = buildBaseQuery(request);

        // 查询内容
        List<Content> contents = contentService.list(queryWrapper);

        // 转换为响应对象并计算得分
        List<RecommendResponse> responses = contents.stream()
                .map(content -> {
                    RecommendResponse response = convertToResponse(content);
                    response.setScore(scoreCalculator.apply(content));
                    response.setReason(reason);
                    return response;
                })
                .collect(Collectors.toList());

        // 按得分排序
        if (needSort) {
            responses = responses.stream()
                    .sorted(Comparator.comparing(RecommendResponse::getScore).reversed())
                    .collect(Collectors.toList());
        }

        return responses;
    }

    /**
     * 构建基础查询条件
     */
    private LambdaQueryWrapper<Content> buildBaseQuery(RecommendRequest request) {
        LambdaQueryWrapper<Content> queryWrapper = new LambdaQueryWrapper<>();

        // 只查询启用状态的内容
        queryWrapper.eq(Content::getStatus, 1);

        // 根据内容类型过滤
        if (request.getType() != null) {
            queryWrapper.eq(Content::getType, request.getType());
        }

        return queryWrapper;
    }

    /**
     * 计算热度得分
     * 公式：阅读量 * 1 + 点赞数 * 2 + 收藏数 * 3
     */
    private double calculateHotScore(Content content) {
        int viewCount = content.getViewCount() != null ? content.getViewCount() : 0;
        int likeCount = content.getLikeCount() != null ? content.getLikeCount() : 0;
        int collectCount = content.getCollectCount() != null ? content.getCollectCount() : 0;

        return viewCount * 1.0 + likeCount * 2.0 + collectCount * 3.0;
    }

    /**
     * 计算新鲜度得分
     * 公式：基于内容创建时间，越新的内容得分越高
     */
    private double calculateFreshnessScore(Content content) {
        if (content.getCreateTime() == null) {
            return 0.0;
        }

        // 计算内容创建时间距今的天数
        long daysSinceCreation = Duration.between(content.getCreateTime(), LocalDateTime.now()).toDays();

        // 新鲜度得分：1天内100分，30天内线性递减，30天后10分
        if (daysSinceCreation <= 1) {
            return 100.0;
        } else if (daysSinceCreation <= 30) {
            return 100.0 - (daysSinceCreation - 1) * 3.0;
        } else {
            return 10.0;
        }
    }

    /**
     * 应用多样性过滤
     * 确保推荐列表中内容类型、分类的多样性
     */
    private List<RecommendResponse> applyDiversityFilter(List<RecommendResponse> recommendations, RecommendConfig config) {
        if (recommendations == null || recommendations.isEmpty() || config.getDiversityWeight() < 50) {
            return recommendations;
        }

        // 按分类分组，确保每个分类都有一定数量的推荐
        Map<Long, List<RecommendResponse>> groupedByCategory = recommendations.stream()
                .collect(Collectors.groupingBy(RecommendResponse::getCategoryId));

        List<RecommendResponse> diversified = new ArrayList<>();
        int maxPerCategory = Math.max(3, recommendations.size() / groupedByCategory.size() + 1);

        // 从每个分类中选取一定数量的内容
        for (List<RecommendResponse> categoryItems : groupedByCategory.values()) {
            diversified.addAll(categoryItems.stream()
                    .limit(maxPerCategory)
                    .toList());
        }

        // 按得分重新排序
        return diversified.stream()
                .sorted(Comparator.comparing(RecommendResponse::getScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 将Content实体转换为RecommendResponse
     */
    private RecommendResponse convertToResponse(Content content) {
        RecommendResponse response = buildBaseResponse(content);
        enrichWithCategoryInfo(response, content.getCategoryId());
        enrichWithTypeSpecificInfo(response, content);
        return response;
    }

    /**
     * 构建基础响应对象
     */
    private RecommendResponse buildBaseResponse(Content content) {
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
        return response;
    }

    /**
     * 丰富分类信息
     */
    private void enrichWithCategoryInfo(RecommendResponse response, Long categoryId) {
        if (categoryId != null) {
            ContentCategory category = contentCategoryService.getById(categoryId);
            if (category != null) {
                response.setCategoryName(category.getCategoryName());
            }
        }
    }

    /**
     * 根据类型丰富详细信息
     */
    private void enrichWithTypeSpecificInfo(RecommendResponse response, Content content) {
        if (content.getType() == 1) {
            enrichWithArticleInfo(response, content.getId());
        } else if (content.getType() == 2) {
            enrichWithVideoInfo(response, content.getId());
        }
    }

    /**
     * 丰富文章信息
     */
    private void enrichWithArticleInfo(RecommendResponse response, Long contentId) {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Article::getContentId, contentId);
        Article article = articleService.getOne(articleQuery);
        if (article != null) {
            response.setContent(article.getContent());
            response.setAuthor(article.getAuthor());
            response.setSource(article.getSource());
        }
    }

    /**
     * 丰富视频信息
     */
    private void enrichWithVideoInfo(RecommendResponse response, Long contentId) {
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getContentId, contentId);
        Video video = videoService.getOne(videoQuery);
        if (video != null) {
            response.setDuration(video.getDuration());
            response.setCoverId(video.getCoverId());

            // 获取封面图URL
            if (video.getCoverId() != null) {
                File coverFile = fileService.getById(video.getCoverId());
                if (coverFile != null) {
                    response.setCoverUrl(coverFile.getFilePathUrl());
                }
            }
        }
    }
}