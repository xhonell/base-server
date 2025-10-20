package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName BannerResponse
 * description:
 * author: xhonell
 * create: 2025年10月20日22时31分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BannerResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long fileId;

    private String filePathUrl;

    private String title;

    private Boolean status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
