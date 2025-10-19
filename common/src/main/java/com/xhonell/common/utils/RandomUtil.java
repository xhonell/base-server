package com.xhonell.common.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * program: BaseServer
 * ClassName RandomUtils
 * description:
 * author: xhonell
 * create: 2025年10月19日16时06分
 * Version 1.0
 **/
public class RandomUtil {

    private static final String NUMBER = "0123456789";
    private static final String LETTER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTER_NUMBER = LETTER + NUMBER;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成指定长度的随机数字字符串
     */
    public static String randomNumber(int length) {
        return randomString(NUMBER, length);
    }

    /**
     * 生成指定长度的随机字母字符串
     */
    public static String randomLetter(int length) {
        return randomString(LETTER, length);
    }

    /**
     * 生成指定长度的随机字母+数字字符串
     */
    public static String randomLetterAndNumber(int length) {
        return randomString(LETTER_NUMBER, length);
    }

    /**
     * 生成指定长度的验证码（数字+大小写字母）
     */
    public static String randomCode(int length) {
        return randomLetterAndNumber(length);
    }

    /**
     * 生成随机 UUID（去除短横线）
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成随机布尔值
     */
    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * 通用的随机字符串生成器
     */
    private static String randomString(String base, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(base.length());
            sb.append(base.charAt(index));
        }
        return sb.toString();
    }
}
