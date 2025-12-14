package com.xhonell.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * program: BaseServer
 * ClassName ConfigProperties
 * description:
 * author: xhonell
 * create: 2025年10月26日22时22分
 * Version 1.0
 **/
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

    private Long avatarId;

    private List<Long> superAdminIds;


}
