package com.xhonell.admin.interceptor;

import com.xhonell.common.annotation.NoAuth;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.properties.RedisPrefixProperties;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.common.utils.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName AuthInterceptor
 * description:
 * author: xhonell
 * create: 2025年10月19日16时46分
 * Version 1.0
 **/
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final RedisUtil redisUtil;

    public AuthInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断 handler 是否为方法
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();

        // 如果方法或类上有 @NoAuth 注解，跳过鉴权
        if (method.isAnnotationPresent(NoAuth.class) || handlerMethod.getBeanType().isAnnotationPresent(NoAuth.class)) {
            return true;
        }

        // 从请求头获取 token
        String token = request.getHeader("Authorization");
        AssertUtil.isTrue(StringUtils.hasText(token), SystemErrorEnum.USER_NOT_LOGIN);

        token = token.substring(7); // 去掉 Bearer

        RedisUser redisUser = redisUtil.get(String.format(RedisPrefixProperties.LOGIN_INFO, token), RedisUser.class);

        // 解析 token 获取用户ID
        AssertUtil.isTrue(Objects.nonNull(redisUser), SystemErrorEnum.USER_NOT_LOGIN);


        // 注入到 ThreadLocal
        RedisUserUtil.set(redisUser);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RedisUserUtil.remove();
    }
}