package com.xhonell.admin.controller;

import com.xhonell.admin.service.ContentCategoryService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.ContentCategory;
import com.xhonell.common.domain.request.ContentCategoryPageRequest;
import com.xhonell.common.domain.request.UpdateStatusRequest;
import com.xhonell.common.domain.response.ContentCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ContentCategoryController
 * description: 内容分类Controller
 * author: xhonell
 * create: 2026年3月7日
 * Version 1.0
 **/
@RestController
@RequestMapping("/content-category")
@RequiredArgsConstructor
public class ContentCategoryController {

    private final ContentCategoryService contentCategoryService;

    @GetMapping("/list")
    public Result<List<ContentCategoryResponse>> list(ContentCategoryPageRequest request) {
        return Result.success(contentCategoryService.selectList(request));
    }

    @GetMapping("/parent-list")
    public Result<List<ContentCategoryResponse>> getParentCategoryList() {
        return Result.success(contentCategoryService.getParentCategoryList());
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody ContentCategory contentCategory) {
        contentCategoryService.saveBy(contentCategory);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody ContentCategory contentCategory) {
        contentCategoryService.updateBy(contentCategory);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<String> status(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        contentCategoryService.updateStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        contentCategoryService.removeById(id);
        return Result.success();
    }
}