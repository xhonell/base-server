package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.PointsRecord;
import com.xhonell.common.domain.entity.UserInfo;
import com.xhonell.common.domain.request.PointsChangeRequest;
import com.xhonell.common.domain.response.PointsChangeResponse;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.server.mapper.PointsRecordMapper;
import com.xhonell.server.service.DailyPointsRecordService;
import com.xhonell.server.service.PointsRecordService;
import com.xhonell.server.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

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
            throw new RuntimeException("今日积分已达到上限");
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
}
