package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.PeContentService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.PeContent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * program: BaseServer
 * ClassName PeContentController
 * description: 教育内容Controller
 * author: xhonell
 * create: 2026年1月18日
 * Version 1.0
 **/
@RestController
@RequestMapping("/peContent")
@RequiredArgsConstructor
public class PeContentController {

    private final PeContentService peContentService;

    @GetMapping("/list")
    public Result<PageInfo<PeContent>> list(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(peContentService.selectList(page, pageSize));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody PeContent peContent) {
        peContentService.saveBy(peContent);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody PeContent peContent) {
        peContentService.updateBy(peContent);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<String> status(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        peContentService.updateStatus(id, (Boolean) body.get("status"));
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        peContentService.removeById(id);
        return Result.success();
    }
}