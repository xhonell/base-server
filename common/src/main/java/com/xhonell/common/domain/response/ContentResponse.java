package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName ContentResponse
 * description: 内容响应类，用于返回内容相关信息及关联信息
 * author: xhonell
 * create: 2026年3月8日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentResponse {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容类型（1=文章，2=视频）
     */
    private Byte type;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 内容简介
     */
    private String description;

    /**
     * 文件主键（若为视频则存储视频ID）
     */
    private Long fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 难度等级ID
     */
    private Long difficultyId;

    /**
     * 难度等级名称
     */
    private String difficultyName;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 政治面貌ID
     */
    private Long politicId;

    /**
     * 政治面貌名称
     */
    private String politicName;

    /**
     * 状态（1 启用，0 禁用）
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}