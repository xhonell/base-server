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
 * ClassName UserInfo
 * description:
 * author: xhonell
 * create: 2025年10月24日23时06分
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_user_info")
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号（外键，关联用户登录表）
     */
    private Long userId;

    /**
     * 政治面貌（1 群众 2 团员 3 预备党员 4 党员 5 其他）
     */
    private Integer politics;

    /**
     * 积分
     */
    private Long integral;

    /**
     * 性别（0 未知 1 男 2 女）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDateTime birthday;

    /**
     * 最后登录城市
     */
    private String lastLogiCity;

    /**
     * 最后登录 IP
     */
    private String lastLoginIp;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
