package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Difficulty;
import com.xhonell.common.domain.request.DifficultyPageRequest;
import com.xhonell.common.domain.request.DifficultySaveRequest;
import com.xhonell.common.domain.response.SelectOption;

import java.util.List;

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

    void saveBy(DifficultySaveRequest request);

    void updateBy(DifficultySaveRequest request);

    void updateStatus(Long id, Boolean status);

    /**
     * 获取启用状态的难度下拉列表
     */
    List<SelectOption> selectEnabledList();
}
