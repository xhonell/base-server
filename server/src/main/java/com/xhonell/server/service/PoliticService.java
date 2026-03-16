package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.PoliticPageRequest;
import com.xhonell.common.domain.response.SelectOption;

import java.util.List;

/**
 * program: BaseServer
 * ClassName PoliticService
 * description:
 * author: xhonell
 * create: 2025年10月24日21时11分
 * Version 1.0
 **/
public interface PoliticService extends IService<Politic> {
    PageInfo<Politic> selectList(PoliticPageRequest request);

    void saveBy(Politic politic);

    void updateBy(Politic politic);

    void updateStatus(Long id, Boolean status);

    /**
     * 获取启用状态的政治面貌下拉列表
     */
    List<SelectOption> selectEnabledList();
}
