package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Role;
import com.xhonell.common.domain.request.RolePageRequest;
import com.xhonell.common.domain.request.RoleSaveRequest;
import com.xhonell.common.domain.response.RoleDetailResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName RoleService
 * description:
 * author: xhonell
 * create: 2025年11月01日18时04分
 * Version 1.0
 **/
public interface RoleService extends IService<Role> {
    PageInfo<Role> selectList(RolePageRequest request);

    void saveBy(RoleSaveRequest request);

    void updateBy(RoleSaveRequest request);

    void updateStatus(Long id, Boolean status);

    void deleteById(Long id);

    RoleDetailResponse detail(Long id);

    List<Role> selectByStatus(List<Long> roleIds, Boolean aTrue);
}
