package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * ClassName PePermission
 * description: 权限表（菜单/接口）
 * author: xhonell
 * create: 2025年11月01日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_permission")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限标识（如 system:user:view）
     */
    private String code;

    /**
     * 权限类型（1 菜单 2 按钮/接口）
     */
    private Byte type;

    /**
     * 前端路由路径（菜单类型用）
     */
    private String path;

    /**
     * 父级ID（用于菜单层级）
     */
    private Long parentId;

    /**
     * 状态（1 启用，0 禁用）
     */
    private Boolean status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}