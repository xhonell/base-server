package com.xhonell.common.utils;

import java.util.regex.Pattern;

/**
 * program: BaseServer
 * ClassName RegexUtil
 * description:
 * author: xhonell
 * create: 2025年10月19日02时27分
 * Version 1.0
 **/
public class RegexUtil {

    private RegexUtil() {
        // 私有构造，防止实例化
    }

    // 手机号（中国大陆）
    private static final String MOBILE_REGEX = "^1[3-9]\\d{9}$";

    // 邮箱
    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    // 身份证（15位或18位）
    private static final String ID_CARD_REGEX = "^(\\d{15}|\\d{17}[\\dXx])$";

    // 用户名（字母开头，允许字母、数字、下划线，长度4-16）
    private static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{3,15}$";

    // 密码（字母+数字+特殊字符，长度8-20）
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";

    // URL
    private static final String URL_REGEX = "^(https?://)?([\\w.-]+)\\.([a-zA-Z]{2,6})([/\\w .-]*)*/?$";

    // IP 地址
    private static final String IP_REGEX = "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$";

    // 验证手机号
    public static boolean isMobile(String mobile) {
        return mobile != null && Pattern.matches(MOBILE_REGEX, mobile);
    }

    // 验证邮箱
    public static boolean isEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    // 验证身份证
    public static boolean isIdCard(String idCard) {
        return idCard != null && Pattern.matches(ID_CARD_REGEX, idCard);
    }

    // 验证用户名
    public static boolean isUsername(String username) {
        return username != null && Pattern.matches(USERNAME_REGEX, username);
    }

    // 验证密码
    public static boolean isPassword(String password) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }

    // 验证 URL
    public static boolean isUrl(String url) {
        return url != null && Pattern.matches(URL_REGEX, url);
    }

    // 验证 IP 地址
    public static boolean isIp(String ip) {
        return ip != null && Pattern.matches(IP_REGEX, ip);
    }

    // 自定义正则校验
    public static boolean matches(String regex, String value) {
        return value != null && Pattern.matches(regex, value);
    }
}
