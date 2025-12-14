package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    List<UserRole> selectByUserId(Long userId);
}
