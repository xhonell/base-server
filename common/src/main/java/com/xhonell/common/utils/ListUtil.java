package com.xhonell.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName ListUtil
 * description:
 * author: xhonell
 * create: 2025年10月19日23时40分
 * Version 1.0
 **/
public class ListUtil {
    /**
     * 将 List<S> 转换为 List<T>
     */
    public static <S, T> List<T> toList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return sourceList.stream().map(source -> {
            try {
                T target = targetClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, target);
                return target;
            } catch (Exception e) {
                throw new RuntimeException("ListConvertUtil.toList failed", e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 使用自定义转换函数将 List<S> 转换为 List<T>
     */
    public static <S, T> List<T> toList(List<S> sourceList, Function<S, T> mapper) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return sourceList.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 将 List<S> 转换为 Set<T>
     */
    public static <S, T> Set<T> toSet(List<S> sourceList, Function<S, T> mapper) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new HashSet<>();
        }
        return sourceList.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * 将 List<S> 转换为 Map<K, V>
     */
    public static <S, K, V> Map<K, V> toMap(List<S> sourceList, Function<S, K> keyMapper, Function<S, V> valueMapper) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new HashMap<>();
        }
        return sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v2));
    }

    /**
     * 将 List<S> 转换为任意类型（如 JSON 字符串、分页对象等）
     */
    public static <S, R> R convert(List<S> sourceList, Function<List<S>, R> converter) {
        return converter.apply(sourceList == null ? Collections.emptyList() : sourceList);
    }
}
