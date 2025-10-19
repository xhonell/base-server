package com.xhonell.common.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName RedisUser
 * description:
 * author: xhonell
 * create: 2025年10月19日16时09分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedisUser {
    private Long id;

    private String username;

    private String email;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 角色（1 普通用户 2 教师 3 系统管理员）
     */
    private Integer role;

    private Long avatarId;

    private String phone;

    private Boolean status;

    private String avatarUrl;
}
