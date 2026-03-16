package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName RecommendResponse
 * description: 推荐响应类
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendResponse {

    /**
     * 内容ID
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
     * 封面ID
     */
    private Long fileId;

    /**
     * 封面URL
     */
    private String fileUrl;

    /**
     * 视频ID
     */
    private Long coverId;

    /**
     * 视频URL
     */
    private String coverUrl;

    /**
     * 视频时长（秒）
     */
    private Long duration;

    /**
     * 文章内容
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
     * 阅读量/播放量
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
     * 标签ID
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 难度ID
     */
    private Long difficultyId;

    /**
     * 难度名称
     */
    private String difficultyName;

    /**
     * 难度对应积分
     */
    private Integer difficultyScore;

    /**
     * 推荐得分（用于排序）
     */
    private Double score;

    /**
     * 推荐理由
     */
    private String reason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}