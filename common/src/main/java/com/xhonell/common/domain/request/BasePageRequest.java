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
 * ClassName BasePageRequest
 * description:
 * author: xhonell
 * create: 2025年10月19日23时30分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasePageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码，默认第 1 页
     */
    protected Integer page = 1;

    /**
     * 每页数量，默认 10 条
     */
    protected Integer pageSize = 10;

    /**
     * 排序字段，可选
     */
    protected String orderBy;

    /**
     * 排序方向，可选：ASC 或 DESC
     */
    protected String orderType = "ASC";
}
