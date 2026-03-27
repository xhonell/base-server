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

    /**
     * 查询用户当前积分
     *
     * @param userId 用户ID
     * @return 当前积分
     */
    Long getUserPoints(Long userId);

    /**
     * 给某个用户增加积分 并返回增加后的积分
     *
     * @param userId  用户ID
     * @param points  增加的积分数量
     * @return 增加后的积分
     */
    Long addPoints(Long userId, Integer points);
}
