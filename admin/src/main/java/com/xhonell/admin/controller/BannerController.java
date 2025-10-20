package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.BannerService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.request.BannerPageRequest;
import com.xhonell.common.domain.response.BannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Result<PageInfo<BannerResponse>> list(BannerPageRequest request) {
        return Result.success( bannerService.selectList(request));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Banner banner) {
         bannerService.saveBy(banner);
         return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Banner banner) {
         bannerService.updateBy(banner);
         return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<String> status(@PathVariable Long id, @RequestBody Map<String, Object> body )  {
        bannerService.updateStatus(id, (Boolean) body.get("status"));
         return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.success();
    }
}
