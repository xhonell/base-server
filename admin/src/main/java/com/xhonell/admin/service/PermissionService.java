package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.Permission;
import com.xhonell.common.domain.request.PermissionPageRequest;
import com.xhonell.common.domain.response.PermissionTreeResponse;

import java.util.List;

/**
 * program: BaseServer
 * ClassName PermissionService
 * description:
 * author: xhonell
 * create: 2025年11月01日18时04分
 * Version 1.0
 **/
public interface PermissionService extends IService<Permission> {

    List<Permission> selectList(PermissionPageRequest request);

    void saveBy(Permission banner);

    void updateBy(Permission banner);

    void updateStatus(Long id, Boolean status);

    List<PermissionTreeResponse> selectTreeByStatus();

    void deleteById(Long id);

    /**
     * 获取当前登录用户的权限树
     * @return 权限树列表
     */
    List<PermissionTreeResponse> selectCurrentUserPermissionTree();
}
