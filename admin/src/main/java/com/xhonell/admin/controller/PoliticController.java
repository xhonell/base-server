package com.xhonell.admin.controller;

import com.github.pagehelper.PageInfo;
import com.xhonell.admin.service.PoliticService;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.PoliticPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Result<PageInfo<Politic>> list(PoliticPageRequest request) {
        return Result.success( politicService.selectList(request));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Politic politic) {
        politicService.saveBy(politic);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Politic politic) {
        politicService.updateBy(politic);
        return Result.success();
    }

    @PostMapping("/status/{id}")
    public Result<String> status(@PathVariable Long id, @RequestBody Map<String, Object> body )  {
        politicService.updateStatus(id, (Boolean) body.get("status"));
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        politicService.removeById(id);
        return Result.success();
    }
}
