package com.xhonell.admin.service.impl;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.UserMapper;
import com.xhonell.admin.service.FileService;
import com.xhonell.admin.service.UserService;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.User;
import com.xhonell.common.domain.request.AdminCreateRequest;
import com.xhonell.common.domain.request.UserPageRequest;
import com.xhonell.common.enums.user.RoleEnum;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.ListUtil;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.common.utils.RedisUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private final FileService fileService;

    @Override
    public User getByEmail(String email, Integer role) {
        AssertUtil.isTrue(Objects.nonNull(email), "邮箱不可为空");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        queryWrapper.eq(User::getRole, role);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public PageInfo<RedisUser> selectList(UserPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<User> users = selectListBy(request);
        List<Long> fileIds = users.stream().map(User::getAvatarId).distinct().toList();
        if (CollectionUtils.isEmpty(fileIds)) {
            return PageUtils.toPageInfo(Lists.newArrayList());
        }
        List<File> files = fileService.listByIds(fileIds);
        Map<Long, String> fileMap = ListUtil.toMap(files, File::getId, File::getFilePathUrl);
        List<RedisUser> redisUsers = ListUtil.toList(users, RedisUser.class);
        redisUsers.forEach(redisUser -> {
            redisUser.setAvatarUrl(fileMap.get(redisUser.getAvatarId()));
            // 手机号脱敏（例如：138****5678）
            if (redisUser.getPhone() != null && redisUser.getPhone().length() == 11) {
                String phone = redisUser.getPhone();
                redisUser.setPhone(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }

            // 邮箱脱敏（例如：x***g@163.com）
            if (redisUser.getEmail() != null && redisUser.getEmail().contains("@")) {
                String email = redisUser.getEmail();
                int atIndex = email.indexOf("@");
                if (atIndex > 2) {
                    String prefix = email.substring(0, atIndex);
                    String maskedPrefix = prefix.charAt(0) + "***" + prefix.charAt(prefix.length() - 1);
                    redisUser.setEmail(maskedPrefix + email.substring(atIndex));
                } else {
                    // 邮箱太短则简单替换
                    redisUser.setEmail("***" + email.substring(atIndex));
                }
            }
        });
        return PageUtils.toPageInfo(redisUsers);
    }

    public List<User> selectListBy(UserPageRequest request) {
        RedisUser redisUser = RedisUserUtil.get();
        request.setRole(redisUser.getSupperAdmin() ? request.getRole() : RoleEnum.USER.getCode());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(request.getRole()), User::getRole, request.getRole());
        queryWrapper.like(StringUtils.hasText(request.getEmail()), User::getEmail, request.getEmail());
        queryWrapper.like(StringUtils.hasText(request.getUsername()), User::getUsername, request.getUsername());
        queryWrapper.ne(User::getId, redisUser.getId());
        queryWrapper.orderByDesc(User::getId);
        return this.list(queryWrapper);
    }

    @Override
    public void createAdmin(AdminCreateRequest request) {
        // 验证密码
        AssertUtil.isTrue(request.isPasswordConfirmed(), "密码不一致");

        // 验证邮箱是否已存在
        User existUser = getByEmail(request.getEmail(), request.getRole());
        AssertUtil.isTrue(Objects.isNull(existUser), "该邮箱已存在");

        // 获取当前登录用户
        RedisUser currentUser = RedisUserUtil.get();
        AssertUtil.isTrue(Objects.nonNull(currentUser), "用户未登录");

        // 权限验证：只有超级管理员可以创建系统管理员
        if (request.getRole().equals(RoleEnum.ADMIN.getCode()) && !currentUser.getSupperAdmin()) {
            AssertUtil.isTrue(false, "只有超级管理员可以创建系统管理员");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setStatus(true); // 默认启用

        // 生成密码盐值和加密密码
        String salt = com.xhonell.common.utils.PasswordUtil.generateSalt();
        String encryptPassword = com.xhonell.common.utils.PasswordUtil.encrypt(request.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);

        // 设置头像
        if (request.getAvatarId() != null) {
            user.setAvatarId(request.getAvatarId());
        }

        // 保存用户
        save(user);
    }
}
