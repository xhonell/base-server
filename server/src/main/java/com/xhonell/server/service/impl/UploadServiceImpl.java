package com.xhonell.server.service.impl;

import com.xhonell.common.domain.entity.File;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.exception.BizException;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.CosUploadUtil;
import com.xhonell.server.service.FileService;
import com.xhonell.server.service.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * program: BaseServer
 * ClassName UploadServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月19日00时58分
 * Version 1.0
 **/
@AllArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {
    private final FileService fileService;

    private final CosUploadUtil cosUploadUtil;
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        AssertUtil.isTrue(Objects.nonNull(file), "请勿上传空文件");
        try {
            File localFile = cosUploadUtil.uploadAuto(file, file.getInputStream());
            fileService.save(localFile);
            return localFile.getFilePathUrl();
        } catch (Exception e) {
            throw new BizException(SystemErrorEnum.UPLOAD_FAILED);
        }
    }
}
