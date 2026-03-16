package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.AgeRange;
import com.xhonell.common.domain.entity.Tag;
import com.xhonell.common.domain.request.TagPageRequest;
import com.xhonell.common.domain.request.TagSaveRequest;
import com.xhonell.common.domain.response.SelectOption;
import com.xhonell.common.domain.response.TagResponse;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.server.mapper.TagMapper;
import com.xhonell.server.service.AgeRangeService;
import com.xhonell.server.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName TagServiceImpl
 * description: 标签Service实现
 * author: xhonell
 * create: 2026年3月8日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final AgeRangeService ageRangeService;

    @Override
    public PageInfo<TagResponse> selectListByRequest(TagPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());
        List<Tag> tags = selectListByRequestParams(request);
        List<TagResponse> tagResponses = convertToResponseList(tags);
        return PageUtils.toPageInfo(tagResponses);
    }

    @Override
    public void saveBy(TagSaveRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setDescription(request.getDescription());
        tag.setAgeRangeId(request.getAgeRangeId());
        tag.setStatus(request.getStatus());
        save(tag);
    }

    @Override
    public void updateBy(TagSaveRequest request) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getId, request.getId());
        updateWrapper.set(Objects.nonNull(request.getName()), Tag::getName, request.getName());
        updateWrapper.set(Objects.nonNull(request.getDescription()), Tag::getDescription, request.getDescription());
        updateWrapper.set(Objects.nonNull(request.getAgeRangeId()), Tag::getAgeRangeId, request.getAgeRangeId());
        updateWrapper.set(Objects.nonNull(request.getStatus()), Tag::getStatus, request.getStatus());
        update(updateWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getId, id);
        updateWrapper.set(Tag::getStatus, status ? (byte) 1 : 0);
        update(updateWrapper);
    }

    @Override
    public List<SelectOption> selectEnabledList() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getStatus, (byte) 1);
        queryWrapper.orderByAsc(Tag::getId);
        List<Tag> tags = baseMapper.selectList(queryWrapper);
        return tags.stream()
                .map(tag -> new SelectOption(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 根据请求参数查询标签列表
     */
    private List<Tag> selectListByRequestParams(TagPageRequest request) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();

        // 根据标签名称查询
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            queryWrapper.like(Tag::getName, request.getName());
        }

        // 根据适用年龄段ID查询
        if (request.getAgeRangeId() != null) {
            queryWrapper.eq(Tag::getAgeRangeId, request.getAgeRangeId());
        }

        // 根据状态查询
        if (request.getStatus() != null) {
            queryWrapper.eq(Tag::getStatus, request.getStatus());
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
            queryWrapper.orderByDesc(Tag::getId);
        }

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 将Tag实体列表转换为TagResponse列表
     */
    private List<TagResponse> convertToResponseList(List<Tag> tags) {
        // 提取所有需要的ID列表
        List<Long> ageRangeIds = tags.stream()
                .map(Tag::getAgeRangeId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询关联数据
        Map<Long, String> ageRangeMap = getAgeRangeMap(ageRangeIds);

        // 转换为Response对象
        return tags.stream()
                .map(tag -> {
                    TagResponse response = new TagResponse();
                    response.setId(tag.getId());
                    response.setName(tag.getName());
                    response.setDescription(tag.getDescription());
                    response.setAgeRangeId(tag.getAgeRangeId());
                    response.setAgeRangeName(ageRangeMap.get(tag.getAgeRangeId()));
                    response.setStatus(tag.getStatus());
                    response.setCreateTime(tag.getCreateTime());
                    response.setUpdateTime(tag.getUpdateTime());
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取年龄段名称映射
     */
    private Map<Long, String> getAgeRangeMap(List<Long> ageRangeIds) {
        if (ageRangeIds == null || ageRangeIds.isEmpty()) {
            return Map.of();
        }

        List<AgeRange> ageRanges = ageRangeService.listByIds(ageRangeIds);
        return ageRanges.stream()
                .collect(Collectors.toMap(AgeRange::getId, AgeRange::getName));
    }
}