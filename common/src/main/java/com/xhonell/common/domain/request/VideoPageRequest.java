package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName VideoPageRequest
 * description: 视频分页查询请求类
 * author: xhonell
 * create: 2026年3月16日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoPageRequest extends BasePageRequest{

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 最小时长（秒）
     */
    private Long minDuration;

    /**
     * 最大时长（秒）
     */
    private Long maxDuration;

    /**
     * 排序方式：1-最新发布，2-最多点赞，3-最多观看
     */
    private Integer sortBy = 1;
}