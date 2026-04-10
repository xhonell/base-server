package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * program: BaseServer
 * ClassName ContentSaveRequest
 * description: 内容保存请求
 * author: xhonell
 * create: 2026年3月9日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class /**/ContentSaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
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
     * 内容简介
     */
    private String description;

    /**
     * 文件主键（若为视频则存储视频ID）
     */
    private Long fileId;

    /**
     * 难度等级ID
     */
    private Long difficultyId;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 政治面貌ID
     */
    private Long politicId;

    /**
     * 状态（1 启用，0 禁用）
     */
    private Byte status;

    // ========== 文章相关字段（仅当type=1时使用） ==========

    /**
     * 文章内容（富文本）
     */
    private String content;

    /**
     * 作者
     */
    private String author;

    /**
     * 来源
     */
    private String source;

    // ========== 视频相关字段（仅当type=2时使用） ==========

    /**
     * 视频时长（秒）
     */
    private Long duration;

    /**
     * 视频封面图ID
     */
    private Long coverId;

    /**
     * 视频分辨率（如：1920x1080）
     */
    private String resolution;

    /**
     * 视频格式（如：mp4, avi）
     */
    private String format;

    /**
     * 视频大小（字节）
     */
    private Long size;

}