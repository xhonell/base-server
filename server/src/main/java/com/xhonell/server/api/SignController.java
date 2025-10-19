package com.xhonell.server.api;

import com.xhonell.common.domain.dto.Result;
import com.xhonell.server.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * program: BaseServer
 * ClassName UploadController
 * description:
 * author: xhonell
 * create: 2025年10月19日00时58分
 * Version 1.0
 **/
@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    /**
     * 签到
     */
    @GetMapping("/today")
    public Result<Void> signToday() {
        signService.signToday();
        return Result.success();
    }

    /**
     * 获取签到记录
     * @return
     */
    @GetMapping("/month")
    public Result<List<Integer>> signMonth() {
        return Result.success(signService.signMonth());
    }

}
