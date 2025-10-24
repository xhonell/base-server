package com.xhonell.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * program: BaseServer
 * ClassName IpUtil
 * description:
 * author: xhonell
 * create: 2025年10月24日22时36分
 * Version 1.0
 **/
public class IpUtil {

    /**
     * 获取客户端真实 IP 地址（支持反向代理）
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        // 依次检查常见的代理头部字段
        String[] headers = {
                "x-forwarded-for",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For 可能包含多个 IP，第一个才是客户端 IP
                if (ip.contains(",")) {
                    return ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        // 如果都没取到，再用 request.getRemoteAddr()
        String ip = request.getRemoteAddr();

        // 处理 localhost 情况
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }
}