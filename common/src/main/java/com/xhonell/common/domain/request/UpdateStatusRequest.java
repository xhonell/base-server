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
 * ClassName UpdateStatusRequest
 * description: 通用状态更新请求参数
 * author: xhonell
 * create: 2026年3月7日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStatusRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态值
     */
    private Boolean status;
}