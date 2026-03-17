package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.ContentService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.request.ContentPageRequest;
import com.xhonell.common.domain.request.ContentSaveRequest;
import com.xhonell.common.domain.request.UpdateStatusRequest;
import com.xhonell.common.domain.response.ContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName ContentController
 * description: 教育内容Controller
 * author: xhonell
 * create: 2026年1月18日
 * Version 1.0
 **/
@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/list")
    @RequirePermission("admin:content:list")
    public Result<PageInfo<ContentResponse>> list(ContentPageRequest request) {
        return Result.success(contentService.selectListByRequest(request));
    }

    @PostMapping("/save")
    @RequirePermission("admin:content:save")
    public Result<String> save(@RequestBody ContentSaveRequest request) {
        contentService.saveBy(request);
        return Result.success();
    }

    @PostMapping("/update")
    @RequirePermission("admin:content:update")
    public Result<String> update(@RequestBody ContentSaveRequest request) {
        contentService.updateBy(request);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    @RequirePermission("admin:content:status")
    public Result<String> status(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        contentService.updateStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission("admin:content:delete")
    public Result<String> delete(@PathVariable Long id) {
        contentService.removeById(id);
        return Result.success();
    }
}