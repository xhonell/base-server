package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.User;
import com.xhonell.server.mapper.UserMapper;
import com.xhonell.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * program: BaseServer
 * ClassName UserServiceImpl
 * description:
 * author: xhonell
 * create: 2025年10月17日23时49分
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
