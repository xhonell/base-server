package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.FileMapper;
import com.xhonell.admin.service.FileService;
import com.xhonell.common.domain.entity.File;
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
