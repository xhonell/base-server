package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * program: BaseServer
 * ClassName User
 * description:
 * author: xhonell
 * create: 2025年10月17日23时46分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String salt;

    @TableField("create_time")
    private Long createTime;

    @TableField("update_time")
    private Long updateTime;

    /**
     * 角色（1 普通用户 2 教师 3 系统管理员）
     */
    private Integer role;
}
