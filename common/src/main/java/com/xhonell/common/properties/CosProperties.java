package com.xhonell.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * program: BaseServer
 * ClassName CosProperties
 * description:
 * author: xhonell
 * create: 2025年10月19日02时59分
 * Version 1.0
 **/
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "cos")
public class CosProperties {
    private String secretId;
    private String secretKey;
    private String region;
    private String bucket;
    private String basePath;
}