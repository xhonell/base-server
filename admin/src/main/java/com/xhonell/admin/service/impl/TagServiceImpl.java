package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.admin.mapper.TagMapper;
import com.xhonell.admin.service.TagService;
import com.xhonell.common.domain.entity.Tag;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName TagServiceImpl
 * description: 标签Service实现
 * author: xhonell
 * create: 2026年3月8日
 * Version 1.0
 **/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}