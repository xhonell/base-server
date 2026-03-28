package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.DailyPointsRecord;
import com.xhonell.server.mapper.DailyPointsRecordMapper;
import com.xhonell.server.service.DailyPointsRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName DailyPointsRecordServiceImpl
 * description: 每日积分记录服务实现
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class DailyPointsRecordServiceImpl extends ServiceImpl<DailyPointsRecordMapper, DailyPointsRecord> implements DailyPointsRecordService {

    // 积分限制配置
    private static final int COMMENT_MAX_POINTS = 5;  // 评论每日最多5积分
    private static final int LIKE_MAX_POINTS = 5;     // 点赞每日最多5积分
    private static final int SiGN_MAX_POINTS = -1;     // 点赞每日最多5积分
    private static final int COLLECT_MAX_POINTS = 5;  // 收藏每日最多5积分
    private static final int ARTICLE_MAX_POINTS = -1; // 文章学习不限制
    private static final int VIDEO_MAX_POINTS = -1;    // 视频学习不限制

    @Override
    public boolean checkAndUpdateDailyPoints(Long userId, Integer sourceType, Integer points) {
        LocalDate today = LocalDate.now();

        // 查询今日记录
        LambdaQueryWrapper<DailyPointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DailyPointsRecord::getUserId, userId);
        queryWrapper.eq(DailyPointsRecord::getSourceType, sourceType);
        queryWrapper.eq(DailyPointsRecord::getRecordDate, today);
        DailyPointsRecord record = getOne(queryWrapper);

        // 检查积分限制
        int currentPoints = record == null ? 0 : record.getPoints();
        int maxPoints = getMaxPointsByType(sourceType);

        if (maxPoints >= 0 && currentPoints >= maxPoints) {
            return false; // 已达到每日上限
        }

        if (maxPoints >= 0 && currentPoints + points > maxPoints) {
            return false; // 超过每日上限
        }

        // 更新或创建记录
        if (record == null) {
            record = new DailyPointsRecord();
            record.setUserId(userId);
            record.setSourceType(sourceType);
            record.setPoints(points);
            record.setCount(1);
            record.setRecordDate(today);
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            save(record);
        } else {
            LambdaUpdateWrapper<DailyPointsRecord> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(DailyPointsRecord::getId, record.getId());
            updateWrapper.set(DailyPointsRecord::getPoints, currentPoints + points);
            updateWrapper.set(DailyPointsRecord::getCount, record.getCount() + 1);
            updateWrapper.set(DailyPointsRecord::getUpdateTime, LocalDateTime.now());
            update(updateWrapper);
        }

        return true;
    }

    @Override
    public Integer getTodayPointsByType(Long userId, Integer sourceType) {
        LocalDate today = LocalDate.now();

        LambdaQueryWrapper<DailyPointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DailyPointsRecord::getUserId, userId);
        queryWrapper.eq(DailyPointsRecord::getSourceType, sourceType);
        queryWrapper.eq(DailyPointsRecord::getRecordDate, today);
        DailyPointsRecord record = getOne(queryWrapper);

        return record == null ? 0 : record.getPoints();
    }

    @Override
    public Integer getTodayCountByType(Long userId, Integer sourceType) {
        LocalDate today = LocalDate.now();

        LambdaQueryWrapper<DailyPointsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DailyPointsRecord::getUserId, userId);
        queryWrapper.eq(DailyPointsRecord::getSourceType, sourceType);
        queryWrapper.eq(DailyPointsRecord::getRecordDate, today);
        DailyPointsRecord record = getOne(queryWrapper);

        return record == null ? 0 : record.getCount();
    }

    /**
     * 根据来源类型获取每日最大积分
     *
     * @param sourceType 来源类型
     * @return 每日最大积分（-1表示不限制）
     */
    private int getMaxPointsByType(Integer sourceType) {
        return switch (sourceType) {
            case 1 -> ARTICLE_MAX_POINTS;   // 文章学习不限制
            case 2 -> VIDEO_MAX_POINTS;     // 视频学习不限制
            case 3 -> COMMENT_MAX_POINTS;   // 评论每日最多5积分
            case 4 -> LIKE_MAX_POINTS;      // 点赞每日最多5积分
            case 5 -> COLLECT_MAX_POINTS;   // 收藏每日最多5积分
            case 6 -> SiGN_MAX_POINTS;   // 签到不限制
            default -> 0;
        };
    }
}