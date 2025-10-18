package com.xhonell.server.api;

import com.qcloud.cos.model.UploadResult;
import com.xhonell.common.domain.dto.Result;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.utils.CosUploadUtil;
import com.xhonell.server.service.UploadService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class UploadController {

    private final UploadService uploadService;

    /***
     * 上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String localFileUrl = uploadService.upload(file);
        return Result.success(localFileUrl);
    }
}
