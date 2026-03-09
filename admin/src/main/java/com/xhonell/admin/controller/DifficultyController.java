package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.AgeRangeService;
import com.xhonell.admin.service.DifficultyService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.AgeRange;
import com.xhonell.common.domain.entity.Difficulty;
import com.xhonell.common.domain.request.AgeRangePageRequest;
import com.xhonell.common.domain.request.DifficultyPageRequest;
import com.xhonell.common.domain.request.UpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * program: BaseServer
 * ClassName DifficultyController
 * description:
 * author: xhonell
 * create: 2025年10月26日21时42分
 * Version 1.0
 **/
@RestController
@RequestMapping("/difficulty")
@RequiredArgsConstructor
public class DifficultyController {

    private final DifficultyService difficultyService;

    @GetMapping("/list")
    public Result<PageInfo<Difficulty>> difficultyService(DifficultyPageRequest request) {
        return Result.success( difficultyService.selectList(request));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Difficulty difficulty) {
        difficultyService.saveBy(difficulty);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Difficulty difficulty) {
        difficultyService.updateBy(difficulty);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<String> status(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        difficultyService.updateStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        difficultyService.removeById(id);
        return Result.success();
    }
}
