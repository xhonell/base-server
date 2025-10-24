package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.UserInfo;

/**
 * program: BaseServer
 * ClassName UserInfoService
 * description:
 * author: xhonell
 * create: 2025年10月24日23时09分
 * Version 1.0
 **/
public interface UserInfoService extends IService<UserInfo> {
    UserInfo getByUserId(Long userId);
}
