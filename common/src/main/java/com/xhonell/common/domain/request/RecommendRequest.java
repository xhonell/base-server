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
     * 用户ID（用于协同过滤推荐）
     */
    private Long userId;

    /**
     * 内容类型（1=文章，2=视频，null=全部）
     */
    private Byte type;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 年龄段ID（用于年龄适配）
     */
    private Long ageRangeId;

    /**
     * 政治面貌ID（用于政治面貌适配）
     */
    private Long politicId;

    /**
     * 推荐数量（如果为null则使用配置中的推荐数量）
     */
    private Integer count;
}