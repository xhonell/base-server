package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xhonell
 * @date 2026/3/26
 * @desc
 */
@Data
@TableName("pe_comment")
public class Comment {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 内容ID（文章/帖子/视频等）
     */
    private Long contentId;

    /**
     * 父评论ID（用于回复功能）
     */
    private Long parentId;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 状态（1正常 0删除）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
