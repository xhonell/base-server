package com.xhonell.server.utils;

/**
 * program: BaseServer
 * ClassName IpRegionUtil
 * description:
 * author: xhonell
 * create: 2025年10月24日22时21分
 * Version 1.0
 **/

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.io.InputStream;

/**
 * IP 地理位置解析工具类
 * 依赖：
 * <dependency>
 * <groupId>org.lionsoul</groupId>
 * <artifactId>ip2region</artifactId>
 * <version>2.6.6</version>
 * </dependency>
 * <p>
 * 示例返回： 中国|0|重庆市|重庆市|联通
 */
@Slf4j
public class IpRegionUtil {

    /**
     * ip2region 数据文件路径（建议放在 resources 目录）
     */
    private static final String DB_PATH = "ip2region_v4.xdb";

    /**
     * Searcher 实例（线程安全）
     */
    private static Searcher searcher;

    static {
        try (InputStream inputStream = IpRegionUtil.class.getClassLoader().getResourceAsStream(DB_PATH)) {

            byte[] dbBytes;

            if (inputStream == null) {
                throw new RuntimeException("未找到资源文件：" + DB_PATH);
            }

            dbBytes = inputStream.readAllBytes();
            searcher = Searcher.newWithBuffer(dbBytes);
            log.info("✅ ip2region 数据库加载成功");

        } catch (IOException e) {
            log.error("❌ 加载 ip2region 数据库失败, 路径:{}", DB_PATH, e);
        }
    }

    /**
     * 根据 IP 获取完整地域信息
     * 示例： 中国|0|重庆市|重庆市|联通
     */
    public static String getRegion(String ip) {
        if (ip == null || ip.isEmpty()) {
            return "未知";
        }
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 获取省份名称
     */
    public static String getProvince(String ip) {
        String region = getRegion(ip);
        String[] parts = region.split("\\|");
        return parts.length >= 3 ? parts[1] : "未知";
    }

    /**
     * 获取城市名称
     */
    public static String getCity(String ip) {
        String region = getRegion(ip);
        String[] parts = region.split("\\|");
        return parts.length >= 4 ? parts[2] : "未知";
    }

    /**
     * 获取运营商
     */
    public static String getIsp(String ip) {
        String region = getRegion(ip);
        String[] parts = region.split("\\|");
        return parts.length >= 5 ? parts[4] : "未知";
    }
}