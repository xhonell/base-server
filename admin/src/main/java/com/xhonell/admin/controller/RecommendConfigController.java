package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.RecommendConfigService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.RecommendConfigPageRequest;
import com.xhonell.common.domain.request.RecommendConfigSaveRequest;
import com.xhonell.common.domain.response.RecommendConfigResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName RecommendConfigController
 * description: 推荐配置Controller
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@RestController
@RequestMapping("/recommend-config")
@RequiredArgsConstructor
public class RecommendConfigController {

    private final RecommendConfigService recommendConfigService;

    @GetMapping("/list")
    @RequirePermission("admin:recommend-config:list")
    public Result<PageInfo<RecommendConfigResponse>> list(RecommendConfigPageRequest request) {
        return Result.success(recommendConfigService.selectListByRequest(request));
    }

    @GetMapping("/active")
    @RequirePermission("admin:recommend-config:active")
    public Result<RecommendConfigResponse> getActiveConfig() {
        return Result.success(recommendConfigService.getActiveConfig());
    }

    @PostMapping("/save")
    @RequirePermission("admin:recommend-config:save")
    public Result<String> save(@RequestBody RecommendConfigSaveRequest request) {
        recommendConfigService.saveBy(request);
        return Result.success();
    }

    @PostMapping("/update")
    @RequirePermission("admin:recommend-config:update")
    public Result<String> update(@RequestBody RecommendConfigSaveRequest request) {
        recommendConfigService.updateBy(request);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission("admin:recommend-config:delete")
    public Result<String> delete(@PathVariable Long id) {
        recommendConfigService.removeById(id);
        return Result.success();
    }
}