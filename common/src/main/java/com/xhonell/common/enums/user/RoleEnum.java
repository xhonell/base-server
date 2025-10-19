package com.xhonell.common.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * program: BaseServer
 * ClassName RoleEnum
 * description:
 * author: xhonell
 * create: 2025年10月19日13时18分
 * Version 1.0
 **/
@Getter
@AllArgsConstructor
public enum RoleEnum {

    USER(1, "普通用户");



    private final Integer code ;

    private final String desc;
}
