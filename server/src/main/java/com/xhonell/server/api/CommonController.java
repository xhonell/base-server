package com.xhonell.server.api;

import com.qcloud.cos.model.UploadResult;
import com.xhonell.common.annotation.NoAuth;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.Banner;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.response.BannerResponse;
import com.xhonell.common.domain.response.UploadFileResponse;
import com.xhonell.common.utils.CosUploadUtil;
import com.xhonell.server.service.BannerService;
import com.xhonell.server.service.UploadService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final UploadService uploadService;

    private final BannerService bannerService;

    /***
     * 上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @NoAuth
    public Result<UploadFileResponse> upload(@RequestParam("file") MultipartFile file) {
        UploadFileResponse localFileUrl = uploadService.upload(file);
        return Result.success(localFileUrl);
    }

    @GetMapping("/banner")
    @NoAuth
    public Result<List<BannerResponse>> banner() {
        return Result.success(bannerService.selectList());
    }
}
