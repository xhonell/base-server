package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Politic;
import com.xhonell.common.domain.request.PoliticPageRequest;

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
}
