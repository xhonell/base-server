package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName File
 * description:
 * author: xhonell
 * create: 2025年10月19日01时40分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_file")
public class File {

    private Long id;

    private String filePath;

    private String fileUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public String getFilePathUrl() {
        return filePath + fileUrl;
    }
}
