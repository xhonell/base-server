package com.xhonell.common.utils;

/**
 * program: BaseServer
 * ClassName PageUtils
 * description:
 * author: xhonell
 * create: 2025年10月19日23时28分
 * Version 1.0
 **/

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * PageHelper 分页工具类
 */
public class PageUtils {

    /**
     * 开启分页
     *
     * @param pageNum  当前页码
     * @param pageSize 每页数量
     */
    public static void startPage(int pageNum, int pageSize) {
        // 防止页码或大小异常
        if (pageNum <= 0) pageNum = 1;
        if (pageSize <= 0) pageSize = 10;
        PageHelper.startPage(pageNum, pageSize);
    }

    /**
     * 封装分页结果
     *
     * @param list 查询出来的数据列表
     * @param <T>  数据类型
     * @return PageInfo 对象
     */
    public static <T> PageInfo<T> toPageInfo(List<T> list) {
        return new PageInfo<>(list);
    }
}