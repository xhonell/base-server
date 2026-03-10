package com.xhonell.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName RecommendConfigResponse
 * description: 推荐配置响应类
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendConfigResponse {

    /**
     * 主键ID
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
     * 算法类型名称
     */
    private String algorithmTypeName;

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
     * 年龄适配名称
     */
    private String ageAdaptName;

    /**
     * 政治面貌适配
     * 1开启 0关闭
     */
    private Integer politicalAdapt;

    /**
     * 政治面貌适配名称
     */
    private String politicalAdaptName;

    /**
     * 状态（1启用，0禁用）
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}