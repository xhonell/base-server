package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * program: BaseServer
 * ClassName PermissionPageRequest
 * description:
 * author: xhonell
 * create: 2025年11月02日21时47分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionPageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long parentId;
}
