package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhonell.common.domain.entity.Content;
import com.xhonell.common.domain.entity.LikeRecord;
import com.xhonell.common.domain.request.LikeRequest;
import com.xhonell.common.exception.BizException;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.server.mapper.ContentMapper;
import com.xhonell.server.mapper.LikeRecordMapper;
import com.xhonell.server.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName LikeServiceImpl
 * description: 点赞服务实现
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class LikeServiceImpl extends ServiceImpl<LikeRecordMapper, LikeRecord> implements LikeService {

    private final ContentMapper contentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleLike(LikeRequest request) {
        Long userId = RedisUserUtil.getUserId();
        Long contentId = request.getContentId();
        Integer operation = request.getOperation();

        // 查询点赞记录
        LambdaQueryWrapper<LikeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LikeRecord::getUserId, userId);
        queryWrapper.eq(LikeRecord::getContentId, contentId);
        LikeRecord existRecord = baseMapper.selectOne(queryWrapper);

        // 查询内容
        Content content = contentMapper.selectById(contentId);
        if (content == null) {
            throw new BizException("内容不存在");
        }

        if (operation == 1) {
            // 点赞操作
            if (existRecord != null && existRecord.getStatus() == 1) {
                throw new BizException("已经点赞过了");
            }

            // 增加点赞数
            content.setLikeCount(content.getLikeCount() == null ? 1 : content.getLikeCount() + 1);
            contentMapper.updateById(content);

            // 插入或更新点赞记录
            if (existRecord == null) {
                existRecord = new LikeRecord();
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
            // 取消点赞操作
            if (existRecord == null || existRecord.getStatus() == 0) {
                throw new BizException("未点赞过");
            }

            // 减少点赞数
            content.setLikeCount(content.getLikeCount() == null || content.getLikeCount() <= 0 ? 0 : content.getLikeCount() - 1);
            contentMapper.updateById(content);

            // 更新点赞记录
            existRecord.setStatus(0);
            existRecord.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(existRecord);
        } else {
            throw new BizException("无效的操作类型");
        }
    }

    @Override
    public Boolean isLiked(Long contentId, Long userId) {
        LambdaQueryWrapper<LikeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LikeRecord::getUserId, userId);
        queryWrapper.eq(LikeRecord::getContentId, contentId);
        queryWrapper.eq(LikeRecord::getStatus, 1);
        return baseMapper.selectCount(queryWrapper) > 0;
    }
}