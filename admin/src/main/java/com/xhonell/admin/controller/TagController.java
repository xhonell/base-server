package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.TagService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.request.TagPageRequest;
import com.xhonell.common.domain.request.TagSaveRequest;
import com.xhonell.common.domain.request.UpdateStatusRequest;
import com.xhonell.common.domain.response.SelectOption;
import com.xhonell.common.domain.response.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName TagController
 * description: 标签Controller
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/list")
    public Result<PageInfo<TagResponse>> list(TagPageRequest request) {
        return Result.success(tagService.selectListByRequest(request));
    }

    @GetMapping("/options")
    public Result<List<SelectOption>> options() {
        return Result.success(tagService.selectEnabledList());
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody TagSaveRequest request) {
        tagService.saveBy(request);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody TagSaveRequest request) {
        tagService.updateBy(request);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<String> status(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        tagService.updateStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.success();
    }
}