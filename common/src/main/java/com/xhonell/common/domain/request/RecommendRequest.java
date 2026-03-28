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
 * ClassName RecommendRequest
 * description: 推荐请求
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 内容类型（1=文章，2=视频，null=全部）
     */
    private Byte type;

    /**
     * 用户ID（用于个性化推荐）
     */
    private Long userId;
}