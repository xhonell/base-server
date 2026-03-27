package com.xhonell.server.api;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.CollectRequest;
import com.xhonell.common.domain.request.InteractionStatusRequest;
import com.xhonell.common.domain.request.LikeRequest;
import com.xhonell.common.domain.response.InteractionStatusResponse;
import com.xhonell.server.service.CollectService;
import com.xhonell.server.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName InteractionController
 * description: 互动功能控制器（点赞、收藏）
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@RestController
@RequestMapping("/interaction")
@RequiredArgsConstructor
public class InteractionController {

    private final LikeService likeService;
    private final CollectService collectService;

    /**
     * 点赞/取消点赞
     *
     * @param request 点赞请求
     * @return 无返回
     */
    @PostMapping("/like")
    public Result<Void> handleLike(@RequestBody LikeRequest request) {
        likeService.handleLike(request);
        return Result.success();
    }

    /**
     * 检查是否已点赞
     *
     * @param contentId 内容ID
     * @return 是否已点赞
     */
    @GetMapping("/like/check/{contentId}")
    public Result<Boolean> checkLike(@PathVariable Long contentId) {
        Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
        Boolean isLiked = likeService.isLiked(contentId, userId);
        return Result.success(isLiked);
    }

    /**
     * 收藏/取消收藏
     *
     * @param request 收藏请求
     * @return 无返回
     */
    @PostMapping("/collect")
    public Result<Void> handleCollect(@RequestBody CollectRequest request) {
        collectService.handleCollect(request);
        return Result.success();
    }

    /**
     * 检查是否已收藏
     *
     * @param contentId 内容ID
     * @return 是否已收藏
     */
    @GetMapping("/collect/check/{contentId}")
    public Result<Boolean> checkCollect(@PathVariable Long contentId) {
        Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
        Boolean isCollected = collectService.isCollected(contentId, userId);
        return Result.success(isCollected);
    }

    /**
     * 获取内容的点赞和收藏状态
     *
     * @param request 互动状态请求
     * @return 互动状态
     */
    @PostMapping("/status")
    public Result<InteractionStatusResponse> getInteractionStatus(@RequestBody InteractionStatusRequest request) {
        Long userId = com.xhonell.common.utils.RedisUserUtil.getUserId();
        Long contentId = request.getContentId();

        Boolean isLiked = likeService.isLiked(contentId, userId);
        Boolean isCollected = collectService.isCollected(contentId, userId);

        InteractionStatusResponse response = new InteractionStatusResponse();
        response.setContentId(contentId);
        response.setIsLiked(isLiked);
        response.setIsCollected(isCollected);

        return Result.success(response);
    }
}