package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.CollectRecord;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.request.CollectRequest;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.server.mapper.ContentMapper;
import com.xhonell.server.mapper.CollectRecordMapper;
import com.xhonell.server.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName CollectServiceImpl
 * description: 收藏服务实现
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class CollectServiceImpl extends ServiceImpl<CollectRecordMapper, CollectRecord> implements CollectService {

    private final ContentMapper contentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleCollect(CollectRequest request) {
        Long userId = RedisUserUtil.getUserId();
        Long contentId = request.getContentId();
        Integer operation = request.getOperation();

        // 查询收藏记录
        LambdaQueryWrapper<CollectRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectRecord::getUserId, userId);
        queryWrapper.eq(CollectRecord::getContentId, contentId);
        CollectRecord existRecord = baseMapper.selectOne(queryWrapper);

        // 查询内容
        Content content = contentMapper.selectById(contentId);
        if (content == null) {
            throw new RuntimeException("内容不存在");
        }

        if (operation == 1) {
            // 收藏操作
            if (existRecord != null && existRecord.getStatus() == 1) {
                throw new RuntimeException("已经收藏过了");
            }

            // 增加收藏数
            content.setCollectCount(content.getCollectCount() == null ? 1 : content.getCollectCount() + 1);
            contentMapper.updateById(content);

            // 插入或更新收藏记录
            if (existRecord == null) {
                existRecord = new CollectRecord();
                existRecord.setUserId(userId);
                existRecord.setContentId(contentId);
                existRecord.setStatus(1);
                existRecord.setCreateTime(LocalDateTime.now());
                existRecord.setUpdateTime(LocalDateTime.now());
                baseMapper.insert(existRecord);
            } else {
                existRecord.setStatus(1);
                existRecord.setUpdateTime(LocalDateTime.now());
                baseMapper.updateById(existRecord);
            }
        } else if (operation == 0) {
            // 取消收藏操作
            if (existRecord == null || existRecord.getStatus() == 0) {
                throw new RuntimeException("未收藏过");
            }

            // 减少收藏数
            content.setCollectCount(content.getCollectCount() == null || content.getCollectCount() <= 0 ? 0 : content.getCollectCount() - 1);
            contentMapper.updateById(content);

            // 更新收藏记录
            existRecord.setStatus(0);
            existRecord.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(existRecord);
        } else {
            throw new RuntimeException("无效的操作类型");
        }
    }

    @Override
    public Boolean isCollected(Long contentId, Long userId) {
        LambdaQueryWrapper<CollectRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectRecord::getUserId, userId);
        queryWrapper.eq(CollectRecord::getContentId, contentId);
        queryWrapper.eq(CollectRecord::getStatus, 1);
        return baseMapper.selectCount(queryWrapper) > 0;
    }
}