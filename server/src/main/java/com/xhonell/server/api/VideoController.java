package com.xhonell.server.api;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.VideoPageRequest;
import com.xhonell.common.domain.response.RecommendResponse;
import com.xhonell.common.domain.response.VideoPageResponse;
import com.xhonell.server.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName VideoController
 * description: 视频相关接口
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /**
     * 视频分页查询
     * @param request 查询请求
     * @return 分页结果
     */
    @PostMapping("/page")
    public Result<VideoPageResponse> pageVideo(@RequestBody VideoPageRequest request) {
        VideoPageResponse response = videoService.pageVideo(request);
        return Result.success(response);
    }

    /**
     * 获取视频详情
     * @param id 视频ID
     * @return 视频详情
     */
    @GetMapping("/detail/{id}")
    public Result<RecommendResponse> getVideoDetail(@PathVariable Long id) {
        RecommendResponse response = videoService.getVideoDetail(id);
        return Result.success(response);
    }
}