package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentCategoryResponse {

    private Long id;

    private String icon;

    private String categoryName;

    private String description;

    private Long weight;

    private Long status;

    private Long parentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    
    private List<ContentCategoryResponse> children;
}