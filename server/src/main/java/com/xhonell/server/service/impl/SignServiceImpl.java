package com.xhonell.server.service.impl;

import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.common.utils.SignUtils;
import com.xhonell.server.service.SignService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * program: BaseServer
 * ClassName SignService
 * description:
 * author: xhonell
 * create: 2025年10月19日22时20分
 * Version 1.0
 **/
@Component
@AllArgsConstructor
public class SignServiceImpl implements SignService {

    private final SignUtils signUtils;

    @Override
    public List<Integer> signMonth() {
        RedisUser redisUser = RedisUserUtil.get();
        return signUtils.getMonthSignRecord(redisUser.getId());
    }

    @Override
    public void signToday() {
        RedisUser redisUser = RedisUserUtil.get();
        signUtils.sign(redisUser.getId());
    }
}
