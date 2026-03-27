package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.DailyPointsRecord;

/**
 * program: BaseServer
 * ClassName DailyPointsRecordService
 * description: 每日积分记录服务
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
public interface DailyPointsRecordService extends IService<DailyPointsRecord> {

    /**
     * 检查并更新每日积分记录
     *
     * @param userId     用户ID
     * @param sourceType 来源类型
     * @param points     积分数量
     * @return 是否允许增加积分
     */
    boolean checkAndUpdateDailyPoints(Long userId, Integer sourceType, Integer points);

    /**
     * 获取用户今日某类型获得的积分数
     *
     * @param userId     用户ID
     * @param sourceType 来源类型
     * @return 今日获得的积分数
     */
    Integer getTodayPointsByType(Long userId, Integer sourceType);

    /**
     * 获取用户今日某类型的操作次数
     *
     * @param userId     用户ID
     * @param sourceType 来源类型
     * @return 今日操作次数
     */
    Integer getTodayCountByType(Long userId, Integer sourceType);
}