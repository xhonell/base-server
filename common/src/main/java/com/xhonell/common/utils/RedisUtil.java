package com.xhonell.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * program: BaseServer
 * ClassName RedisUtil
 * description:
 * author: xhonell
 * create: 2025年10月19日02时34分
 * Version 1.0
 **/
@Slf4j
@Component
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisUtil(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // ==================== String操作 ====================

    public <T> void set(String key, T value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json);
        } catch (JsonProcessingException e) {
            log.error("设置Redis缓存失败,key:{}", key);
        }
    }

    public <T> void set(String key, T value, long timeout) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, timeout, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("设置Redis缓存失败,key:{}", key, e);
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("获取Redis缓存失败,key:{}", key);
            return null;
        }
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // ==================== Hash操作 ====================

    public <T> void hSet(String key, String hashKey, T value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForHash().put(key, hashKey, json);
        } catch (JsonProcessingException e) {
            log.error("设置RedisSet缓存失败,key:{}", key);
        }
    }

    public <T> T hGet(String key, String hashKey, Class<T> clazz) {
        Object obj = redisTemplate.opsForHash().get(key, hashKey);
        if (Objects.isNull( obj)) return null;
        try {
            return objectMapper.readValue(obj.toString(), clazz);
        } catch (JsonProcessingException e) {
            log.error("获取RedisSet缓存失败,key:{}", key);
            return null;
        }
    }

    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(Set.of(keys));
            }
        }
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

}
