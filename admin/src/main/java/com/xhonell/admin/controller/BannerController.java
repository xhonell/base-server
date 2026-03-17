package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.BannerService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.request.BannerPageRequest;
import com.xhonell.common.domain.request.UpdateStatusRequest;
import com.xhonell.common.domain.response.BannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName BannerController
 * description:
 * author: xhonell
 * create: 2025年10月19日22时51分
 * Version 1.0
 **/
@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/list")
    @RequirePermission("admin:banner:list")
    public Result<PageInfo<BannerResponse>> list(BannerPageRequest request) {
        return Result.success( bannerService.selectList(request));
    }

    @PostMapping("/save")
    @RequirePermission("admin:banner:save")
    public Result<String> save(@RequestBody Banner banner) {
         bannerService.saveBy(banner);
         return Result.success();
    }

    @PostMapping("/update")
    @RequirePermission("admin:banner:update")
    public Result<String> update(@RequestBody Banner banner) {
         bannerService.updateBy(banner);
         return Result.success();
    }

    @PostMapping("/status/{id}")
    @RequirePermission("admin:banner:status")
    public Result<String> status(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        bannerService.updateStatus(id, request.getStatus());
         return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission("admin:banner:delete")
    public Result<String> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.success();
    }
}
