package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.PointsRecord;
import com.xhonell.common.domain.entity.Topic;
import com.xhonell.common.domain.entity.UserInfo;
import com.xhonell.common.domain.entity.User;
import com.xhonell.common.domain.request.PointsChangeRequest;
import com.xhonell.common.domain.request.PointsRankingRequest;
import com.xhonell.common.domain.request.PointsDetailRequest;
import com.xhonell.common.domain.request.LearningHistoryRequest;
import com.xhonell.common.domain.response.ActivityResponse;
import com.xhonell.common.domain.response.PointsBySourceResponse;
import com.xhonell.common.domain.response.PointsChartDataResponse;
import com.xhonell.common.domain.response.PointsChangeResponse;
import com.xhonell.common.domain.response.PointsRankingResponse;
import com.xhonell.common.domain.response.PointsStatisticsResponse;
import com.xhonell.common.domain.response.UserStatisticsResponse;
import com.xhonell.common.domain.response.PointsDetailResponse;
import com.xhonell.common.domain.response.LearningHistoryResponse;
import com.xhonell.common.exception.BizException;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.server.mapper.PointsRecordMapper;
import com.xhonell.server.mapper.FileMapper;
import com.xhonell.server.mapper.TopicMapper;
import com.xhonell.server.mapper.UserMapper;
import com.xhonell.server.service.DailyPointsRecordService;
import com.xhonell.server.service.PointsRecordService;
import com.xhonell.server.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xhonell
 * @date 2026/3/27
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord> implements PointsRecordService {

    private final UserInfoService userInfoService;
    private final DailyPointsRecordService dailyPointsRecordService;
    private final UserMapper userMapper;
    private final FileMapper fileMapper;
    private final TopicMapper topicMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PointsChangeResponse addPoints(PointsChangeRequest request) {
        Long userId = RedisUserUtil.getUserId();
        if (Objects.isNull(userId)) {
            return new PointsChangeResponse(0, 0, 0);
        }

        // 解析来源类型
        Integer sourceType = parseSourceType(request.getSourceType());
        Integer points = getPointsBySourceType(sourceType, request.getPoints());

        // 检查每日积分限制
        boolean canAdd = dailyPointsRecordService.checkAndUpdateDailyPoints(userId, sourceType, points);
        if (!canAdd) {
            throw new BizException("今日积分已达到上限");
        }

        // 获取变动前积分
        Long beforePoints = getUserPoints(userId);

        // 更新用户积分
        Long afterPoints = updateUserPoints(userId, points);

        // 创建积分记录
        createPointsRecord(userId, sourceType, points, afterPoints, request);

        log.info("用户积分变动成功，userId: {}, 来源类型: {}, 积分: {}, 变动前: {}, 变动后: {}", userId, sourceType, points, beforePoints, afterPoints);

        // 返回响应
        return new PointsChangeResponse(beforePoints.intValue(), points, afterPoints.intValue());
    }

    /**
     * 获取用户当前积分
     *
     * @param userId 用户ID
     * @return 当前积分
     */
    private Long getUserPoints(Long userId) {
        return userInfoService.getUserPoints(userId);
    }

    /**
     * 解析来源类型
     *
     * @param sourceTypeStr 来源类型字符串
     * @return 来源类型数字
     */
    private Integer parseSourceType(String sourceTypeStr) {
        if (sourceTypeStr == null) {
            return 0;
        }
        return switch (sourceTypeStr.toLowerCase()) {
            case "文章学习", "article", "1" -> 1;
            case "视频学习", "video", "2" -> 2;
            case "评论", "comment", "3" -> 3;
            case "点赞", "like", "4" -> 4;
            case "收藏", "collect", "5" -> 5;
            default -> 0;
        };
    }

    /**
     * 根据来源类型获取积分数量
     *
     * @param sourceType 来源类型
     * @return 积分数量
     */
    private Integer getPointsBySourceType(Integer sourceType, Integer points) {
        return switch (sourceType) {
            case 1 -> points;  // 文章学习10积分
            case 2 -> points;  // 视频学习20积分
            case 3 -> 1;   // 评论1积分
            case 4 -> 1;   // 点赞1积分
            case 5 -> 1;   // 收藏1积分
            default -> 0;
        };
    }

    /**
     * 更新用户积分
     *
     * @param userId 用户ID
     * @param points 积分数量
     * @return 更新后的积分
     */
    private Long updateUserPoints(Long userId, Integer points) {
        // 查询用户信息
        return userInfoService.addPoints(userId, points);
    }

    /**
     * 创建积分记录
     *
     * @param userId      用户ID
     * @param sourceType  来源类型
     * @param points      积分数量
     * @param afterPoints 变动后积分
     * @param request     请求对象
     */
    private void createPointsRecord(Long userId, Integer sourceType, Integer points, Long afterPoints, PointsChangeRequest request) {
        // 查询用户当前积分
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        UserInfo userInfo = userInfoService.getOne(queryWrapper);
        Long beforePoints = userInfo.getIntegral() - points;

        // 创建积分记录
        PointsRecord pointsRecord = new PointsRecord();
        pointsRecord.setUserId(userId);
        pointsRecord.setChangePoints(points);
        pointsRecord.setBeforePoints(beforePoints.intValue());
        pointsRecord.setAfterPoints(afterPoints.intValue());
        pointsRecord.setSourceType(String.valueOf(sourceType));
        pointsRecord.setSourceId(request.getSourceId());
        pointsRecord.setRemark(request.getRemark());
        pointsRecord.setCreateTime(LocalDateTime.now());
        pointsRecord.setUpdateTime(LocalDateTime.now());
        save(pointsRecord);
    }

    @Override
    public List<ActivityResponse> getLatestActivities() {
        // 查询最新的4条积分记录
        LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PointsRecord::getCreateTime);
        queryWrapper.last("LIMIT 4");
        List<PointsRecord> pointsRecords = baseMapper.selectList(queryWrapper);

        if (pointsRecords.isEmpty()) {
            return List.of();
        }

        // 批量查询用户信息
        List<Long> userIds = pointsRecords.stream().map(PointsRecord::getUserId).distinct().collect(Collectors.toList());
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 批量查询头像文件信息
        List<Long> avatarIds = users.stream().map(User::getAvatarId).filter(id -> id != null).distinct().collect(Collectors.toList());
        Map<Long, File> fileMap = avatarIds.isEmpty() ? Map.of() : fileMapper.selectBatchIds(avatarIds).stream()
                .collect(Collectors.toMap(File::getId, file -> file));

        // 构建响应列表
        return pointsRecords.stream().map(record -> {
            ActivityResponse response = new ActivityResponse();
            response.setUserId(record.getUserId());
            response.setCreateTime(record.getCreateTime());

            User user = userMap.get(record.getUserId());
            if (user != null) {
                response.setUserName(user.getUsername());
                if (user.getAvatarId() != null) {
                    File file = fileMap.get(user.getAvatarId());
                    if (file != null) {
                        response.setAvatarUrl(file.getFilePathUrl());
                    }
                }
            }

            // 根据积分变动类型生成描述
            response.setDescription(buildActivityDescription(record));

            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 根据积分记录构建动态描述
     *
     * @param record 积分记录
     * @return 动态描述
     */
    private String buildActivityDescription(PointsRecord record) {
        String sourceType = record.getSourceType();
        Integer changePoints = record.getChangePoints();

        if (changePoints == null) {
            return "参与了积分活动";
        }

        String action = changePoints > 0 ? "获得" : "消费";
        String pointsText = Math.abs(changePoints) + "积分";

        return switch (sourceType) {
            case "1" -> "通过文章学习" + action + pointsText;
            case "2" -> "通过视频学习" + action + pointsText;
            case "3" -> "通过评论" + action + pointsText;
            case "4" -> "通过点赞" + action + pointsText;
            case "5" -> "通过收藏" + action + pointsText;
            default -> action + pointsText;
        };
    }

    @Override
    public PointsStatisticsResponse getPointsStatistics() {
        Long userId = RedisUserUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }

        // 1. 获取当前总积分
        Long totalPoints = getUserPoints(userId);

        // 2. 获取本周获取积分
        Long thisWeekPoints = getThisWeekPoints(userId);

        // 3. 获取积分排名
        Integer ranking = getUserRanking(userId, totalPoints);

        // 构建响应
        return new PointsStatisticsResponse()
                .setTotalPoints(totalPoints)
                .setThisWeekPoints(thisWeekPoints)
                .setRanking(ranking);
    }

    /**
     * 获取本周获取的积分
     *
     * @param userId 用户ID
     * @return 本周获取的积分
     */
    private Long getThisWeekPoints(Long userId) {
        // 计算本周的开始时间（周一 00:00:00）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime weekEnd = now.with(DayOfWeek.SUNDAY).with(LocalTime.MAX);

        // 查询本周的积分记录（只计算正数积分）
        LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsRecord::getUserId, userId);
        queryWrapper.ge(PointsRecord::getCreateTime, weekStart);
        queryWrapper.le(PointsRecord::getCreateTime, weekEnd);
        queryWrapper.gt(PointsRecord::getChangePoints, 0);

        List<PointsRecord> records = baseMapper.selectList(queryWrapper);
        return records.stream()
                .map(PointsRecord::getChangePoints)
                .mapToLong(Integer::longValue)
                .sum();
    }

    /**
     * 获取用户积分排名
     *
     * @param userId      用户ID
     * @param totalPoints 用户总积分
     * @return 排名
     */
    private Integer getUserRanking(Long userId, Long totalPoints) {
        // 查询积分大于当前用户积分的用户数量
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(UserInfo::getIntegral, totalPoints);
        Long count = userInfoService.count(queryWrapper);

        // 排名 = 积分比自己多的人数 + 1
        return count.intValue() + 1;
    }

    @Override
    public PointsBySourceResponse getPointsBySource() {
        Long userId = RedisUserUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }

        // 查询用户的所有积分记录（只统计正数积分）
        LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsRecord::getUserId, userId);
        queryWrapper.gt(PointsRecord::getChangePoints, 0);
        List<PointsRecord> records = baseMapper.selectList(queryWrapper);

        // 按来源类型统计积分
        Long articlePoints = 0L;
        Long videoPoints = 0L;
        Long commentPoints = 0L;
        Long likePoints = 0L;
        Long collectPoints = 0L;
        Long otherPoints = 0L;

        for (PointsRecord record : records) {
            Integer changePoints = record.getChangePoints();
            if (changePoints == null || changePoints <= 0) {
                continue;
            }

            String sourceType = record.getSourceType();
            Long points = changePoints.longValue();

            switch (sourceType) {
                case "1" -> articlePoints += points;
                case "2" -> videoPoints += points;
                case "3" -> commentPoints += points;
                case "4" -> likePoints += points;
                case "5" -> collectPoints += points;
                default -> otherPoints += points;
            }
        }

        // 计算总积分
        Long totalPoints = articlePoints + videoPoints + commentPoints + likePoints + collectPoints + otherPoints;

        // 构建响应
        return new PointsBySourceResponse()
                .setArticlePoints(articlePoints)
                .setVideoPoints(videoPoints)
                .setCommentPoints(commentPoints)
                .setLikePoints(likePoints)
                .setCollectPoints(collectPoints)
                .setOtherPoints(otherPoints)
                .setTotalPoints(totalPoints);
    }

    @Override
    public PointsChartDataResponse getPointsChartData() {
        Long userId = RedisUserUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }

        // 查询用户的所有积分记录（只统计正数积分）
        LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsRecord::getUserId, userId);
        queryWrapper.gt(PointsRecord::getChangePoints, 0);
        List<PointsRecord> records = baseMapper.selectList(queryWrapper);

        // 按来源类型统计积分
        Map<String, Long> sourceMap = new java.util.HashMap<>();
        for (PointsRecord record : records) {
            Integer changePoints = record.getChangePoints();
            if (changePoints == null || changePoints <= 0) {
                continue;
            }

            String sourceType = record.getSourceType();
            Long points = changePoints.longValue();
            sourceMap.put(sourceType, sourceMap.getOrDefault(sourceType, 0L) + points);
        }

        // 计算总积分
        Long totalPoints = sourceMap.values().stream().mapToLong(Long::longValue).sum();

        // 构建图表数据
        List<PointsChartDataResponse.PointsSourceData> sources = new ArrayList<>();
        sources.add(createSourceData("学习文章", sourceMap.get("1"), totalPoints));
        sources.add(createSourceData("学习视频", sourceMap.get("2"), totalPoints));
        sources.add(createSourceData("发表帖子", sourceMap.get("6"), totalPoints));
        sources.add(createSourceData("评论", sourceMap.get("3"), totalPoints));
        sources.add(createSourceData("点赞", sourceMap.get("4"), totalPoints));
        sources.add(createSourceData("收藏", sourceMap.get("5"), totalPoints));

        // 过滤掉积分为0的项
        sources = sources.stream()
                .filter(data -> data.getValue() != null && data.getValue() > 0)
                .collect(Collectors.toList());

        // 构建响应
        return new PointsChartDataResponse()
                .setTotalPoints(totalPoints)
                .setSources(sources);
    }

    /**
     * 创建来源数据
     *
     * @param name       来源名称
     * @param value      积分数量
     * @param totalPoints 总积分
     * @return 来源数据
     */
    private PointsChartDataResponse.PointsSourceData createSourceData(String name, Long value, Long totalPoints) {
        PointsChartDataResponse.PointsSourceData data = new PointsChartDataResponse.PointsSourceData();
        data.setName(name);
        data.setValue(value != null ? value : 0L);
        
        if (totalPoints != null && totalPoints > 0 && value != null && value > 0) {
            double percentage = (double) value / totalPoints * 100;
            data.setPercentage(Math.round(percentage * 100) / 100.0);  // 保留两位小数
        } else {
            data.setPercentage(0.0);
        }
        
        return data;
    }

    @Override
    public UserStatisticsResponse getUserStatistics() {
        Long userId = RedisUserUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }

        // 1. 获取当前积分
        Long points = getUserPoints(userId);

        // 2. 统计学习文章数（从积分记录中统计 sourceType=1 的记录数）
        LambdaQueryWrapper<PointsRecord> articleQueryWrapper = new LambdaQueryWrapper<>();
        articleQueryWrapper.eq(PointsRecord::getUserId, userId);
        articleQueryWrapper.eq(PointsRecord::getSourceType, "1");
        articleQueryWrapper.gt(PointsRecord::getChangePoints, 0);
        Long articleCount = baseMapper.selectCount(articleQueryWrapper);

        // 3. 统计学习视频数（从积分记录中统计 sourceType=2 的记录数）
        LambdaQueryWrapper<PointsRecord> videoQueryWrapper = new LambdaQueryWrapper<>();
        videoQueryWrapper.eq(PointsRecord::getUserId, userId);
        videoQueryWrapper.eq(PointsRecord::getSourceType, "2");
        videoQueryWrapper.gt(PointsRecord::getChangePoints, 0);
        Long videoCount = baseMapper.selectCount(videoQueryWrapper);

        // 4. 统计发表论贴数
        LambdaQueryWrapper<Topic> topicQueryWrapper = new LambdaQueryWrapper<>();
        topicQueryWrapper.eq(Topic::getUserId, userId);
        topicQueryWrapper.eq(Topic::getStatus, 1);  // 只统计正常状态的论贴
        Long topicCount = topicMapper.selectCount(topicQueryWrapper);

        // 构建响应
        return new UserStatisticsResponse()
                .setPoints(points)
                .setArticleCount(articleCount)
                .setVideoCount(videoCount)
                .setTopicCount(topicCount);
    }

    @Override
    public PageInfo<PointsRankingResponse> getPointsRanking(PointsRankingRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());

        // 查询用户积分排行榜
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserInfo::getIntegral);
        List<UserInfo> userInfos = userInfoService.list(queryWrapper);
        PageInfo<UserInfo> pageInfo = PageUtils.toPageInfo(userInfos);

        if (userInfos.isEmpty()) {
            return new PageInfo<>();
        }

        // 批量查询用户信息
        List<Long> userIds = userInfos.stream().map(UserInfo::getUserId).collect(Collectors.toList());
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 批量查询头像文件信息
        List<Long> avatarIds = users.stream().map(User::getAvatarId).filter(id -> id != null).distinct().collect(Collectors.toList());
        Map<Long, File> fileMap = avatarIds.isEmpty() ? Map.of() : fileMapper.selectBatchIds(avatarIds).stream()
                .collect(Collectors.toMap(File::getId, file -> file));

        // 构建响应列表
        List<PointsRankingResponse> rankingList = new java.util.ArrayList<>();
        // 计算全局排名：排名 = (当前页-1) * 每页数量 + 当前页索引 + 1
        int baseRank = (request.getPage() - 1) * request.getPageSize();
        for (int i = 0; i < userInfos.size(); i++) {
            UserInfo userInfo = userInfos.get(i);
            PointsRankingResponse response = new PointsRankingResponse();
            response.setRanking(baseRank + i + 1);  // 全局排名从1开始
            response.setUserId(userInfo.getUserId());
            response.setPoints(userInfo.getIntegral());

            User user = userMap.get(userInfo.getUserId());
            if (user != null) {
                response.setUserName(user.getUsername());
                if (user.getAvatarId() != null) {
                    File file = fileMap.get(user.getAvatarId());
                    if (file != null) {
                        response.setAvatarUrl(file.getFilePathUrl());
                    }
                }
            }

            rankingList.add(response);
        }

        // 构建返回结果
        PageInfo<PointsRankingResponse> resultPageInfo = new PageInfo<>();
        resultPageInfo.setPageNum(pageInfo.getPageNum());
        resultPageInfo.setPageSize(pageInfo.getPageSize());
        resultPageInfo.setSize(pageInfo.getSize());
        resultPageInfo.setTotal(pageInfo.getTotal());
        resultPageInfo.setPages(pageInfo.getPages());
        resultPageInfo.setList(rankingList);

        return resultPageInfo;
    }

    @Override
    public PageInfo<LearningHistoryResponse> getLearningHistory(LearningHistoryRequest request) {
        Long userId = RedisUserUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }

        PageUtils.startPage(request.getPage(), request.getPageSize());

        // 查询学习历史（只查询文章学习和视频学习的记录）
        LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsRecord::getUserId, userId);
        queryWrapper.in(PointsRecord::getSourceType, "1", "2");  // 只查询文章学习和视频学习
        queryWrapper.gt(PointsRecord::getChangePoints, 0);  // 只查询获得的积分

        // 根据来源类型筛选
        if (request.getSourceType() != null) {
            queryWrapper.eq(PointsRecord::getSourceType, String.valueOf(request.getSourceType()));
        }

        queryWrapper.orderByDesc(PointsRecord::getCreateTime);

        List<PointsRecord> records = baseMapper.selectList(queryWrapper);
        PageInfo<PointsRecord> pageInfo = PageUtils.toPageInfo(records);

        // 转换为响应对象
        List<LearningHistoryResponse> responses = records.stream().map(record -> {
            LearningHistoryResponse response = new LearningHistoryResponse();
            response.setId(record.getId());
            response.setSourceType(Integer.parseInt(record.getSourceType()));
            response.setSourceTypeName(getSourceTypeName(record.getSourceType()));
            response.setSourceId(record.getSourceId());
            response.setPoints(record.getChangePoints());
            response.setRemark(record.getRemark());
            response.setCreateTime(record.getCreateTime());
            return response;
        }).collect(Collectors.toList());

        // 构建返回结果
        PageInfo<LearningHistoryResponse> resultPageInfo = new PageInfo<>();
        resultPageInfo.setPageNum(pageInfo.getPageNum());
        resultPageInfo.setPageSize(pageInfo.getPageSize());
        resultPageInfo.setSize(pageInfo.getSize());
        resultPageInfo.setTotal(pageInfo.getTotal());
        resultPageInfo.setPages(pageInfo.getPages());
        resultPageInfo.setList(responses);

        return resultPageInfo;
    }

    @Override
    public PageInfo<PointsDetailResponse> getPointsDetail(PointsDetailRequest request) {
        Long userId = RedisUserUtil.getUserId();
        if (userId == null) {
            throw new BizException("用户未登录");
        }

        PageUtils.startPage(request.getPage(), request.getPageSize());

        // 查询积分明细
        LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsRecord::getUserId, userId);

        // 根据积分变动类型筛选
        if (request.getChangeType() != null) {
            if (request.getChangeType() == 1) {
                queryWrapper.gt(PointsRecord::getChangePoints, 0);  // 只查询增加的积分
            } else if (request.getChangeType() == 2) {
                queryWrapper.lt(PointsRecord::getChangePoints, 0);  // 只查询减少的积分
            }
        }

        queryWrapper.orderByDesc(PointsRecord::getCreateTime);

        List<PointsRecord> records = baseMapper.selectList(queryWrapper);
        PageInfo<PointsRecord> pageInfo = PageUtils.toPageInfo(records);

        // 转换为响应对象
        List<PointsDetailResponse> responses = records.stream().map(record -> {
            PointsDetailResponse response = new PointsDetailResponse();
            response.setId(record.getId());
            response.setChangePoints(record.getChangePoints());
            response.setBeforePoints(record.getBeforePoints());
            response.setAfterPoints(record.getAfterPoints());
            response.setSourceType(record.getSourceType());
            response.setSourceTypeName(getSourceTypeName(record.getSourceType()));
            response.setSourceId(record.getSourceId());
            response.setRemark(record.getRemark());
            response.setCreateTime(record.getCreateTime());
            return response;
        }).collect(Collectors.toList());

        // 构建返回结果
        PageInfo<PointsDetailResponse> resultPageInfo = new PageInfo<>();
        resultPageInfo.setPageNum(pageInfo.getPageNum());
        resultPageInfo.setPageSize(pageInfo.getPageSize());
        resultPageInfo.setSize(pageInfo.getSize());
        resultPageInfo.setTotal(pageInfo.getTotal());
        resultPageInfo.setPages(pageInfo.getPages());
        resultPageInfo.setList(responses);

        return resultPageInfo;
    }

    /**
     * 根据来源类型获取来源类型名称
     *
     * @param sourceType 来源类型
     * @return 来源类型名称
     */
    private String getSourceTypeName(String sourceType) {
        if (sourceType == null) {
            return "未知";
        }
        return switch (sourceType) {
            case "1" -> "文章学习";
            case "2" -> "视频学习";
            case "3" -> "评论";
            case "4" -> "点赞";
            case "5" -> "收藏";
            default -> "其他";
        };
    }
}
