package com.xhonell.server.api;

import com.qcloud.cos.model.UploadResult;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.annotation.NoAuth;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.request.PointsRankingRequest;
import com.xhonell.common.domain.request.PointsDetailRequest;
import com.xhonell.common.domain.request.LearningHistoryRequest;
import com.xhonell.common.domain.response.ActivityResponse;
import com.xhonell.common.domain.response.BannerResponse;
import com.xhonell.common.domain.response.PointsBySourceResponse;
import com.xhonell.common.domain.response.PointsChartDataResponse;
import com.xhonell.common.domain.response.PointsRankingResponse;
import com.xhonell.common.domain.response.PointsStatisticsResponse;
import com.xhonell.common.domain.response.UserStatisticsResponse;
import com.xhonell.common.domain.response.PointsDetailResponse;
import com.xhonell.common.domain.response.LearningHistoryResponse;
import com.xhonell.common.domain.response.UploadFileResponse;
import com.xhonell.common.utils.CosUploadUtil;
import com.xhonell.server.service.BannerService;
import com.xhonell.server.service.PointsRecordService;
import com.xhonell.server.service.UploadService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * program: BaseServer
 * ClassName UploadController
 * description:
 * author: xhonell
 * create: 2025年10月19日00时58分
 * Version 1.0
 **/
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final UploadService uploadService;

    private final BannerService bannerService;

    private final PointsRecordService pointsRecordService;

    /***
     * 上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @NoAuth
    public Result<UploadFileResponse> upload(@RequestParam("file") MultipartFile file) {
        UploadFileResponse localFileUrl = uploadService.upload(file);
        return Result.success(localFileUrl);
    }

    @GetMapping("/banner")
    @NoAuth
    public Result<List<BannerResponse>> banner() {
        return Result.success(bannerService.selectList());
    }

    /**
     * 获取最新动态（从积分记录中获取最新4条）
     * @return 最新动态列表
     */
    @GetMapping("/activities")
    @NoAuth
    public Result<List<ActivityResponse>> getLatestActivities() {
        return Result.success(pointsRecordService.getLatestActivities());
    }

    /**
     * 获取积分统计信息（总积分、本周积分、排名）
     * @return 积分统计信息
     */
    @GetMapping("/points/statistics")
    public Result<PointsStatisticsResponse> getPointsStatistics() {
        return Result.success(pointsRecordService.getPointsStatistics());
    }

    /**
     * 获取按来源统计的积分数量
     * @return 按来源统计的积分
     */
    @GetMapping("/points/by-source")
    public Result<PointsBySourceResponse> getPointsBySource() {
        return Result.success(pointsRecordService.getPointsBySource());
    }

    /**
     * 获取积分图表数据（用于展示积分分布）
     * @return 积分图表数据
     */
    @GetMapping("/points/chart-data")
    public Result<PointsChartDataResponse> getPointsChartData() {
        return Result.success(pointsRecordService.getPointsChartData());
    }

    /**
     * 获取积分排行榜
     * @param request 排行榜请求参数
     * @return 积分排行榜
     */
    @GetMapping("/points/ranking")
    @NoAuth
    public Result<PageInfo<PointsRankingResponse>> getPointsRanking(PointsRankingRequest request) {
        return Result.success(pointsRecordService.getPointsRanking(request));
    }

    /**
     * 获取用户统计数据
     * @return 用户统计数据
     */
    @GetMapping("/user/statistics")
    public Result<UserStatisticsResponse> getUserStatistics() {
        return Result.success(pointsRecordService.getUserStatistics());
    }

    /**
     * 获取学习历史
     * @param request 学习历史请求参数
     * @return 学习历史分页数据
     */
    @PostMapping("/learning/history")
    public Result<PageInfo<LearningHistoryResponse>> getLearningHistory(@RequestBody LearningHistoryRequest request) {
        return Result.success(pointsRecordService.getLearningHistory(request));
    }

    /**
     * 获取积分明细
     * @param request 积分明细请求参数
     * @return 积分明细分页数据
     */
    @PostMapping("/points/detail")
    public Result<PageInfo<PointsDetailResponse>> getPointsDetail(@RequestBody PointsDetailRequest request) {
        return Result.success(pointsRecordService.getPointsDetail(request));
    }
}
