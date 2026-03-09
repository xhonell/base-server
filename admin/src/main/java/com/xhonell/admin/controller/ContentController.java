package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.ContentService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.request.ContentPageRequest;
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
    public Result<PageInfo<ContentResponse>> list(ContentPageRequest request) {
        return Result.success(contentService.selectListByRequest(request));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Content content) {
        contentService.saveBy(content);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Content content) {
        contentService.updateBy(content);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<String> status(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        contentService.updateStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        contentService.removeById(id);
        return Result.success();
    }
}