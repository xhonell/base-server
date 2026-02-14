package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName PeVideo
 * description: 教育视频表
 * author: xhonell
 * create: 2026年1月18日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_video")
public class PeVideo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 内容ID（关联pe_content表）
     */
    private Long contentId;

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

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}