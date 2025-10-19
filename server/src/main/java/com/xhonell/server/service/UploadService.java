package com.xhonell.server.service;

import com.xhonell.common.domain.response.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * program: BaseServer
 * ClassName UploadService
 * description:
 * author: xhonell
 * create: 2025年10月19日00时58分
 * Version 1.0
 **/
public interface UploadService {
    UploadFileResponse upload(MultipartFile file);
}
