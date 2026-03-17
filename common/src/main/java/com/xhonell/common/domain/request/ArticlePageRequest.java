package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName ArticlePageRequest
 * description: 文章分页查询请求类
 * author: xhonell
 * create: 2026年3月17日
 * Version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticlePageRequest extends BasePageRequest{

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 排序方式：1-最新发布，2-最多点赞，3-最多阅读
     */
    private Integer sortBy = 1;
}