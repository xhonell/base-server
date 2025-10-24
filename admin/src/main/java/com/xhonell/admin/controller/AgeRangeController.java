package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.AgeRangeService;
import com.xhonell.admin.service.PoliticService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.AgeRange;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.AgeRangePageRequest;
import com.xhonell.common.domain.request.PoliticPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * program: BaseServer
 * ClassName AgeRangeController
 * description:
 * author: xhonell
 * create: 2025年10月24日21时48分
 * Version 1.0
 **/
@RestController
@RequestMapping("/age-range")
@RequiredArgsConstructor
public class AgeRangeController {

    private final AgeRangeService ageRangeService;

    @GetMapping("/list")
    public Result<PageInfo<AgeRange>> list(AgeRangePageRequest request) {
        return Result.success( ageRangeService.selectList(request));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody AgeRange ageRange) {
        ageRangeService.saveBy(ageRange);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody AgeRange ageRange) {
        ageRangeService.updateBy(ageRange);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        ageRangeService.removeById(id);
        return Result.success();
    }
}
