package com.xhonell.common.utils;

import com.xhonell.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * program: BaseServer
 * ClassName: PasswordUtil
 * description: 密码工具类 - 支持随机盐 + MD5 加密 + 校验
 * author: xhonell
 * create: 2025年10月19日13时29分
 * Version 1.0
 **/
@Slf4j
public class PasswordUtil {

    private static final int SALT_LENGTH = 16;

    /**
     * 生成随机盐（Base64编码）
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 使用盐对密码进行 MD5 加密
     *
     * @param password 原始密码
     * @param salt     盐
     * @return 加密后的密码（32位小写MD5）
     */
    public static String encrypt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String input = password + salt;
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("密码加密失败:{}", e.getMessage(), e);
            throw new BizException("密码加密失败");
        }
    }

    /**
     * 校验密码是否正确
     *
     * @param inputPassword 用户输入的明文密码
     * @param salt          存储的盐
     * @param encryptedPwd  存储的加密密码
     * @return true：密码正确；false：密码错误
     */
    public static boolean verify(String inputPassword, String salt, String encryptedPwd) {
        String encryptedInput = encrypt(inputPassword, salt);
        return encryptedInput.equals(encryptedPwd);
    }
}
