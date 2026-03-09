package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * program: BaseServer
 * ClassName ContentCategoryPageRequest
 * description: 内容分类分页请求参数
 * author: xhonell
 * create: 2026年3月7日
 * Version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ContentCategoryPageRequest extends BasePageRequest {
    @Serial
    private static final long serialVersionUID = 1L;

}