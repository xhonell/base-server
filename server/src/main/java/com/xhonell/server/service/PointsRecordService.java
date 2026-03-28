package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.PointsRecord;
import com.xhonell.common.domain.request.LearningHistoryRequest;
import com.xhonell.common.domain.request.PointsChangeRequest;
import com.xhonell.common.domain.request.PointsDetailRequest;
import com.xhonell.common.domain.request.PointsRankingRequest;
import com.xhonell.common.domain.response.*;

import java.util.List;

/**
 * @author xhonell
 * @date 2026/3/27
 * @desc
 */
public interface PointsRecordService extends IService<PointsRecord> {

    /**
     * 增加用户积分
     *
     * @param request 积分变动请求
     * @return 积分变动响应
     */
    PointsChangeResponse addPoints(PointsChangeRequest request);

    /**
     * 获取最新动态（从积分记录中获取最新4条）
     *
     * @return 最新动态列表
     */
    List<ActivityResponse> getLatestActivities();

    /**
     * 获取积分统计信息（总积分、本周积分、排名）
     *
     * @return 积分统计信息
     */
    PointsStatisticsResponse getPointsStatistics();

    /**
     * 获取按来源统计的积分数量
     *
     * @return 按来源统计的积分
     */
    PointsBySourceResponse getPointsBySource();

    /**
     * 获取积分图表数据（用于展示积分分布）
     *
     * @return 积分图表数据
     */
    PointsChartDataResponse getPointsChartData();

    /**
     * 获取用户统计数据（积分、学习文章数、学习视频数、发表论贴数）
     *
     * @return 用户统计数据
     */
    UserStatisticsResponse getUserStatistics();

    /**
     * 获取积分排行榜
     *
     * @param request 排行榜请求参数
     * @return 积分排行榜
     */
    PageInfo<PointsRankingResponse> getPointsRanking(PointsRankingRequest request);

    /**
     * 获取学习历史
     *
     * @param request 学习历史请求参数
     * @return 学习历史分页数据
     */
    PageInfo<LearningHistoryResponse> getLearningHistory(LearningHistoryRequest request);

    /**
     * 获取积分明细
     *
     * @param request 积分明细请求参数
     * @return 积分明细分页数据
     */
    PageInfo<PointsDetailResponse> getPointsDetail(PointsDetailRequest request);
}
