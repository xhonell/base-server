package com.xhonell.common.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * program: BaseServer
 * ClassName TopicCategoryEnum
 * description: 话题分类枚举
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Getter
@AllArgsConstructor
public enum TopicCategoryEnum {

    DISCUSS(1, "谈论"),
    QA(2, "问答"),
    SHARE(3, "分享"),
    ACTIVITY(4, "活动");

    private final Integer code;

    private final String desc;
}