package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.RecommendConfigMapper;
import com.xhonell.admin.service.RecommendConfigService;
import com.xhonell.common.domain.entity.RecommendConfig;
import com.xhonell.common.domain.request.RecommendConfigPageRequest;
import com.xhonell.common.domain.request.RecommendConfigSaveRequest;
import com.xhonell.common.domain.response.RecommendConfigResponse;
import com.xhonell.common.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * program: BaseServer
 * ClassName RecommendConfigServiceImpl
 * description: 推荐配置Service实现
 * author: xhonell
 * create: 2026年3月10日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RecommendConfigServiceImpl extends ServiceImpl<RecommendConfigMapper, RecommendConfig> implements RecommendConfigService {

    @Override
    public PageInfo<RecommendConfigResponse> selectListByRequest(RecommendConfigPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<RecommendConfig> configs = selectListByRequestParams(request);
        List<RecommendConfigResponse> configResponses = convertToResponseList(configs);
        return PageUtils.toPageInfo(configResponses);
    }

    @Override
    public void saveBy(RecommendConfigSaveRequest request) {
        RecommendConfig config = new RecommendConfig();
        config.setAlgorithmType(request.getAlgorithmType());
        config.setRecommendCount(request.getRecommendCount());
        config.setDiversityWeight(request.getDiversityWeight());
        config.setFreshnessWeight(request.getFreshnessWeight());
        config.setHotWeight(request.getHotWeight());
        config.setAgeAdapt(request.getAgeAdapt());
        config.setPoliticalAdapt(request.getPoliticalAdapt());
        config.setStatus(request.getStatus());
        config.setRuleName(request.getRuleName());
        config.setRuleDesc(request.getRuleDesc());

        // 如果保存时状态为启用，则将其他所有配置的状态改为禁用
        if (Objects.nonNull(request.getStatus()) && request.getStatus() == 1) {
            disableAllConfigs();
        }

        save(config);
    }

    @Override
    public void updateBy(RecommendConfigSaveRequest request) {
        LambdaUpdateWrapper<RecommendConfig> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RecommendConfig::getId, request.getId());
        updateWrapper.set(Objects.nonNull(request.getAlgorithmType()), RecommendConfig::getAlgorithmType, request.getAlgorithmType());
        updateWrapper.set(Objects.nonNull(request.getRecommendCount()), RecommendConfig::getRecommendCount, request.getRecommendCount());
        updateWrapper.set(Objects.nonNull(request.getDiversityWeight()), RecommendConfig::getDiversityWeight, request.getDiversityWeight());
        updateWrapper.set(Objects.nonNull(request.getFreshnessWeight()), RecommendConfig::getFreshnessWeight, request.getFreshnessWeight());
        updateWrapper.set(Objects.nonNull(request.getHotWeight()), RecommendConfig::getHotWeight, request.getHotWeight());
        updateWrapper.set(Objects.nonNull(request.getAgeAdapt()), RecommendConfig::getAgeAdapt, request.getAgeAdapt());
        updateWrapper.set(Objects.nonNull(request.getPoliticalAdapt()), RecommendConfig::getPoliticalAdapt, request.getPoliticalAdapt());
        updateWrapper.set(Objects.nonNull(request.getStatus()), RecommendConfig::getStatus, request.getStatus());
        updateWrapper.set(Objects.nonNull(request.getRuleName()), RecommendConfig::getRuleName, request.getRuleName());
        updateWrapper.set(Objects.nonNull(request.getRuleDesc()), RecommendConfig::getRuleDesc, request.getRuleDesc());
        update(updateWrapper);

        // 如果更新时状态为启用，则将其他所有配置的状态改为禁用
        if (Objects.nonNull(request.getStatus()) && request.getStatus() == 1) {
            LambdaUpdateWrapper<RecommendConfig> disableWrapper = new LambdaUpdateWrapper<>();
            disableWrapper.ne(RecommendConfig::getId, request.getId());
            disableWrapper.set(RecommendConfig::getStatus, 0);
            update(disableWrapper);
        }
    }

    @Override
    public RecommendConfigResponse getActiveConfig() {
        LambdaQueryWrapper<RecommendConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecommendConfig::getStatus, 1);
        queryWrapper.orderByDesc(RecommendConfig::getId);
        queryWrapper.last("LIMIT 1");
        RecommendConfig config = getOne(queryWrapper);
        return config != null ? convertToResponse(config) : null;
    }

    /**
     * 根据请求参数查询推荐配置列表
     */
    private List<RecommendConfig> selectListByRequestParams(RecommendConfigPageRequest request) {
        LambdaQueryWrapper<RecommendConfig> queryWrapper = new LambdaQueryWrapper<>();

        // 根据算法类型查询
        if (request.getAlgorithmType() != null) {
            queryWrapper.eq(RecommendConfig::getAlgorithmType, request.getAlgorithmType());
        }

        // 根据年龄适配查询
        if (request.getAgeAdapt() != null) {
            queryWrapper.eq(RecommendConfig::getAgeAdapt, request.getAgeAdapt());
        }

        // 根据政治面貌适配查询
        if (request.getPoliticalAdapt() != null) {
            queryWrapper.eq(RecommendConfig::getPoliticalAdapt, request.getPoliticalAdapt());
        }

        // 根据状态查询
        if (request.getStatus() != null) {
            queryWrapper.eq(RecommendConfig::getStatus, request.getStatus());
        }

        // 根据排序字段和排序方向进行排序
        if (request.getOrderBy() != null && !request.getOrderBy().trim().isEmpty()) {
            String[] orderFields = request.getOrderBy().split(",");
            if ("DESC".equalsIgnoreCase(request.getOrderType())) {
                queryWrapper.last("ORDER BY " + String.join(" DESC, ", orderFields) + " DESC");
            } else {
                queryWrapper.last("ORDER BY " + String.join(" ASC, ", orderFields) + " ASC");
            }
        } else {
            // 默认按ID降序排列
            queryWrapper.orderByDesc(RecommendConfig::getId);
        }

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 将RecommendConfig实体列表转换为RecommendConfigResponse列表
     */
    private List<RecommendConfigResponse> convertToResponseList(List<RecommendConfig> configs) {
        return configs.stream()
                .map(this::convertToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 将RecommendConfig实体转换为RecommendConfigResponse
     */
    private RecommendConfigResponse convertToResponse(RecommendConfig config) {
        return new RecommendConfigResponse()
                .setId(config.getId())
                .setAlgorithmType(config.getAlgorithmType())
                .setAlgorithmTypeName(getAlgorithmTypeName(config.getAlgorithmType()))
                .setRecommendCount(config.getRecommendCount())
                .setDiversityWeight(config.getDiversityWeight())
                .setFreshnessWeight(config.getFreshnessWeight())
                .setHotWeight(config.getHotWeight())
                .setAgeAdapt(config.getAgeAdapt())
                .setAgeAdaptName(getAdaptName(config.getAgeAdapt()))
                .setPoliticalAdapt(config.getPoliticalAdapt())
                .setPoliticalAdaptName(getAdaptName(config.getPoliticalAdapt()))
                .setStatus(config.getStatus())
                .setStatusName(getStatusName(config.getStatus()))
                .setCreateTime(config.getCreateTime())
                .setUpdateTime(config.getUpdateTime())
                .setRuleName(config.getRuleName())
                .setRuleDesc(config.getRuleDesc());
    }

    /**
     * 获取算法类型名称
     */
    private String getAlgorithmTypeName(Integer algorithmType) {
        if (algorithmType == null) {
            return null;
        }
        return switch (algorithmType) {
            case 1 -> "热门推荐";
            case 2 -> "最新推荐";
            case 3 -> "混合推荐";
            case 4 -> "协同过滤";
            default -> "未知";
        };
    }

    /**
     * 获取适配状态名称
     */
    private String getAdaptName(Integer adapt) {
        if (adapt == null) {
            return null;
        }
        return adapt == 1 ? "开启" : "关闭";
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) {
            return null;
        }
        return status == 1 ? "启用" : "禁用";
    }

    /**
     * 禁用所有配置
     */
    private void disableAllConfigs() {
        LambdaUpdateWrapper<RecommendConfig> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(RecommendConfig::getStatus, 0);
        update(updateWrapper);
    }
}