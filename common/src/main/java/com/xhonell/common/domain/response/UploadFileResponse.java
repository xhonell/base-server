package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * program: BaseServer
 * ClassName UploadFileResponse
 * description:
 * author: xhonell
 * create: 2025年10月19日12时42分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadFileResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long fileId;

    private String filePathUrl;
}
