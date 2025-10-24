package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName Content
 * description:
 * author: xhonell
 * create: 2025年10月24日21时07分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_content")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
