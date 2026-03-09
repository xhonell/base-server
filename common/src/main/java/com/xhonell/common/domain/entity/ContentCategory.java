package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_content_category")
public class ContentCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String icon;

    private String categoryName;

    private String description;

    private Long weight;

    private Long status;

    private Long parentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
