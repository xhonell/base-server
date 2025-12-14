package com.xhonell.common.domain.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xhonell.common.domain.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * program: BaseServer
 * ClassName PermissionTreeResponse
 * description:
 * author: xhonell
 * create: 2025年11月02日22时40分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionTreeResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
     * 子权限
     */
    private List<PermissionTreeResponse> childrenPermission;
}
