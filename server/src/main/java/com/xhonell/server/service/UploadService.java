package com.xhonell.server.service;

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
    String upload(MultipartFile file);
}
