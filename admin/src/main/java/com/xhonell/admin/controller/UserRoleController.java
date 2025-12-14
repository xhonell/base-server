package com.xhonell.admin.controller;

import com.xhonell.admin.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * program: BaseServer
 * ClassName UserRoleMapper
 * description:
 * author: xhonell
 * create: 2025年11月01日18时14分
 * Version 1.0
 **/
@RestController
@RequestMapping("/user-role")
@RequiredArgsConstructor
public class UserRoleController {
    private final UserRoleService userRoleService;


}
