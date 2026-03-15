package com.xhonell.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * program: BaseServer
 * ClassName RecommendConfigSaveRequest
 * description: 推荐配置保存请求
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendConfigSaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 推荐算法类型
     * 1=热门推荐
     * 2=最新推荐
     * 3=混合推荐
     * 4=协同过滤
     */
    private Integer algorithmType;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则描述
     */
    private String ruleDesc;

    /**
     * 推荐数量
     */
    private Integer recommendCount;

    /**
     * 多样性权重(0-100)
     */
    private Integer diversityWeight;

    /**
     * 新鲜度权重(0-100)
     */
    private Integer freshnessWeight;

    /**
     * 热度权重(0-100)
     */
    private Integer hotWeight;

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
     * 同一时间只能有一个配置为启用状态
     */
    private Integer status;
}