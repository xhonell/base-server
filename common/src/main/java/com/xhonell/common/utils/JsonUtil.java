package com.xhonell.common.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类 —— 同时支持 Gson 与 Fastjson
 * 支持对象、List、Map 的序列化与反序列化
 *
 * @author xhonell
 * @since 2025-11-01
 */
public class JsonUtil {

    // Gson 实例（格式化时间、支持 null 值）
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * 对象转 JSON 字符串
     */
    public static String toJsonByGson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * JSON 字符串转对象
     */
    public static <T> T fromJsonByGson(String json, Class<T> clazz) {
        try {
            return GSON.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Gson 解析异常: " + e.getMessage());
        }
    }

    /**
     * JSON 转泛型对象（如 List、Map）
     */
    public static <T> T fromJsonByGson(String json, Type type) {
        return GSON.fromJson(json, type);
    }


    /**
     * 对象转 JSON 字符串（Fastjson）
     */
    public static String toJsonByFastjson(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * JSON 字符串转对象（Fastjson）
     */
    public static <T> T fromJsonByFastjson(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

    /**
     * JSON 转 List
     */
    public static <T> List<T> toListByFastjson(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * JSON 转 Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMapByFastjson(String json) {
        return JSONObject.parseObject(json, Map.class);
    }


    /**
     * JSON 格式化输出
     */
    public static String formatJson(String json) {
        Object jsonObject = JSON.parse(json);
        return JSON.toJSONString(jsonObject, JSONWriter.Feature.PrettyFormat);
    }

    /**
     * 判断字符串是否为合法 JSON
     */
    public static boolean isValidJson(String json) {
        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}