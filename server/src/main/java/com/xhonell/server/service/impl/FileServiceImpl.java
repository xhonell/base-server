package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.File;
import com.xhonell.server.mapper.FileMapper;
import com.xhonell.server.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName FileServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月19日01时55分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
}
