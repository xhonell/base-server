package com.xhonell.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.User;
import jakarta.validation.constraints.Email;

import java.util.List;

/**
 * program: BaseServer
 * ClassName UserService
 * description:
 * author: xhonell
 * create: 2025年10月17日23时49分
 * Version 1.0
 **/
public interface UserService extends IService<User> {
    User getByEmail(@Email(message = "邮箱格式不正确") String email, Integer role);

    List<RedisUser> selectList();
}
