package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.PoliticService;
import com.xhonell.common.annotation.RequirePermission;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.PoliticPageRequest;
import com.xhonell.common.domain.request.UpdateStatusRequest;
import com.xhonell.common.domain.response.SelectOption;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName PoliticController
 * description:
 * author: xhonell
 * create: 2025年10月24日21时12分
 * Version 1.0
 **/
@RestController
@RequestMapping("/politic")
@RequiredArgsConstructor
public class PoliticController {
    private final PoliticService politicService;

    @GetMapping("/list")
    @RequirePermission("admin:politic:list")
    public Result<PageInfo<Politic>> list(PoliticPageRequest request) {
        return Result.success( politicService.selectList(request));
    }

    @GetMapping("/options")
    public Result<List<SelectOption>> options() {
        return Result.success(politicService.selectEnabledList());
    }

    @PostMapping("/save")
    @RequirePermission("admin:politic:save")
    public Result<String> save(@RequestBody Politic politic) {
        politicService.saveBy(politic);
        return Result.success();
    }

    @PostMapping("/update")
    @RequirePermission("admin:politic:update")
    public Result<String> update(@RequestBody Politic politic) {
        politicService.updateBy(politic);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    @RequirePermission("admin:politic:status")
    public Result<String> status(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        politicService.updateStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission("admin:politic:delete")
    public Result<String> delete(@PathVariable Long id) {
        politicService.removeById(id);
        return Result.success();
    }
}
