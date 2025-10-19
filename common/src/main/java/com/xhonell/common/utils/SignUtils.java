package com.xhonell.common.utils;

import com.xhonell.common.properties.RedisPrefixProperties;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * program: BaseServer
 * ClassName SignUtils
 * description:
 * author: xhonell
 * create: 2025年10月19日22时12分
 * Version 1.0
 **/
@Component
@AllArgsConstructor
public class SignUtils {

    private final RedisUtil redisUtil;

    /**
     * 签到
     * @param userId
     */
    public void sign(Long userId) {
        LocalDate today = LocalDate.now();
        String key = getRedisKey(userId, today);
        int offset = today.getDayOfMonth() - 1;
        redisUtil.setBit(key, offset, true);
    }

    /**
     * 今日是否签到
     * @param userId
     * @return
     */
    public boolean isSigned(Long userId) {
        LocalDate today = LocalDate.now();
        String key = getRedisKey(userId, today);
        int offset = today.getDayOfMonth() - 1;
        return redisUtil.getBit(key, offset);
    }

    /**
     * 获取连续签到天数
     * @param userId
     * @return
     */
    public int getContinuousDays(Long userId) {
        LocalDate today = LocalDate.now();
        String key = getRedisKey(userId, today);
        int day = today.getDayOfMonth();
        int count = 0;

        // 从今天往前查
        for (int i = day - 1; i >= 0; i--) {
            Boolean signed = redisUtil.getBit(key, i);
            if (signed) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    /**
     * 查询本月签到记录
     * @param userId
     * @return
     */
    public List<Integer> getMonthSignRecord(Long userId) {
        LocalDate today = LocalDate.now();
        String key = getRedisKey(userId, today);
        int daysInMonth = today.lengthOfMonth();
        List<Integer> record = new ArrayList<>();

        for (int i = 0; i < daysInMonth; i++) {
            Boolean signed = redisUtil.getBit(key, i);
            record.add(signed ? 1 : 0);
        }
        return record;
    }

    /**
     * 获取key
     * @param userId
     * @param date
     * @return
     */
    private String getRedisKey(Long userId, LocalDate date) {
        return  String.format(RedisPrefixProperties.USER_TOKEN_PREFIX, userId)
                + String.format(RedisPrefixProperties.SIGN_IN_PREFIX,date.format(DateTimeFormatter.ofPattern("yyyy-MM")));
    }
}
