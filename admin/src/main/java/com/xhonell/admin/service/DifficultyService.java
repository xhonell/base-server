package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Difficulty;
import com.xhonell.common.domain.request.DifficultyPageRequest;

/**
 * program: BaseServer
 * ClassName DifficultyService
 * description:
 * author: xhonell
 * create: 2025年10月26日21时33分
 * Version 1.0
 **/
public interface DifficultyService extends IService<Difficulty> {

    PageInfo<Difficulty> selectList(DifficultyPageRequest request);

    void saveBy(Difficulty difficulty);

    void updateBy(Difficulty difficulty);

    void updateStatus(Long id, Boolean status);
}
