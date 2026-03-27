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
     * 文件图片
     */
    private String fileImage;

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
     * 阅读量
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 是否已点赞
     */
    private Boolean isLiked;

    /**
     * 是否已收藏
     */
    private Boolean isCollected;

    // ========== 视频相关字段（仅当type=2时有值） ==========

    /**
     * 视频时长（秒）
     */
    private Long duration;

    /**
     * 视频封面图ID
     */
    private Long coverId;

    /**
     * 视频封面图URL
     */
    private String coverUrl;

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

    // ========== 文章相关字段（仅当type=1时有值） ==========

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
