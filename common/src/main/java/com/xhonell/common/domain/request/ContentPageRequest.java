package com.xhonell.common.domain.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName ContentPageRequest
 * description: 内容分页请求类
 * author: xhonell
 * create: 2026年3月8日
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentPageRequest extends BasePageRequest {

    // 可以在这里添加内容相关的特定查询条件字段
    private String title; // 标题查询
    private Byte type; // 内容类型
    private Long categoryId; // 分类ID
    private Long difficultyId; // 难度等级ID
    private Long politicId; // 政治面貌ID
    private Byte status; // 状态
}