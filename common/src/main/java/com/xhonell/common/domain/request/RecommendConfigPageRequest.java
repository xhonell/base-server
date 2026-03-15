package com.xhonell.common.domain.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * program: BaseServer
 * ClassName RecommendConfigPageRequest
 * description: 推荐配置分页请求类
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class RecommendConfigPageRequest extends BasePageRequest {

    /**
     * 推荐算法类型
     * 1=热门推荐
     * 2=最新推荐
     * 3=混合推荐
     * 4=协同过滤
     */
    private Integer algorithmType;

    /**
     * 年龄适配
     * 1开启 0关闭
     */
    private Integer ageAdapt;

    /**
     * 政治面貌适配
     * 1开启 0关闭
     */
    private Integer politicalAdapt;

    /**
     * 状态（1启用，0禁用）
     */
    private Integer status;
}