package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * program: BaseServer
 * ClassName TopicCommentResponse
 * description: 话题评论响应类（包含用户信息）
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicCommentResponse {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像URL
     */
    private String avatarUrl;

    /**
     * 话题ID
     */
    private Long topicId;

    /**
     * 父评论ID
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

    /**
     * 子评论列表
     */
    private List<TopicCommentResponse> children;
}