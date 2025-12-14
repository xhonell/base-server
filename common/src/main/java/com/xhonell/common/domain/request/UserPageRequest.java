package com.xhonell.common.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * program: BaseServer
 * ClassName UserListRequest
 * description:
 * author: xhonell
 * create: 2025年10月19日23时30分
 * Version 1.0
 **/
@Getter
@Setter
public class UserPageRequest extends BasePageRequest {

    private Integer role;

    private String username;

    private String email;
}
