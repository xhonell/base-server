package com.xhonell.server.api;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.PointsChangeRequest;
import com.xhonell.common.domain.response.DailyPointsResponse;
import com.xhonell.common.domain.response.PointsChangeResponse;
import com.xhonell.server.service.DailyPointsRecordService;
import com.xhonell.server.service.PointsRecordService;
import com.xhonell.server.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName PointsController
 * description: 积分相关接口
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointsController {

    private final PointsRecordService pointsRecordService;
    private final UserInfoService userInfoService;
    private final DailyPointsRecordService dailyPointsRecordService;

    /**
     * 增加用户积分
     *
     * @param request 积分变动请求
     * @return 变动后的积分
     */
    @PostMapping("/add")
    public Result<PointsChangeResponse> addPoints(@RequestBody PointsChangeRequest request) {
        return Result.success(pointsRecordService.addPoints(request));
    }

    /**
     * 查询用户当前积分
     *
     * @param userId 用户ID
     * @return 当前积分
     */
    @GetMapping("/current/{userId}")
    public Result<Long> getUserPoints(@PathVariable Long userId) {
        Long points = userInfoService.getUserPoints(userId);
        return Result.success(points);
    }

    /**
     * 查询用户今日积分统计
     *
     * @param userId 用户ID
     * @return 今日积分统计
     */
    @GetMapping("/daily/{userId}")
    public Result<DailyPointsResponse> getDailyPoints(@PathVariable Long userId) {
        DailyPointsResponse response = new DailyPointsResponse();
        response.setUserId(userId);

        // 获取各类型积分
        response.setArticlePoints(dailyPointsRecordService.getTodayPointsByType(userId, 1));
        response.setArticleCount(dailyPointsRecordService.getTodayCountByType(userId, 1));

        response.setVideoPoints(dailyPointsRecordService.getTodayPointsByType(userId, 2));
        response.setVideoCount(dailyPointsRecordService.getTodayCountByType(userId, 2));

        response.setCommentPoints(dailyPointsRecordService.getTodayPointsByType(userId, 3));
        response.setCommentCount(dailyPointsRecordService.getTodayCountByType(userId, 3));

        response.setLikePoints(dailyPointsRecordService.getTodayPointsByType(userId, 4));
        response.setLikeCount(dailyPointsRecordService.getTodayCountByType(userId, 4));

        response.setCollectPoints(dailyPointsRecordService.getTodayPointsByType(userId, 5));
        response.setCollectCount(dailyPointsRecordService.getTodayCountByType(userId, 5));

        // 计算总积分
        Integer total = response.getArticlePoints() + response.getVideoPoints()
                + response.getCommentPoints() + response.getLikePoints() + response.getCollectPoints();
        response.setTotalPoints(total);

        return Result.success(response);
    }
}