package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Comment;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.User;
import com.xhonell.common.domain.request.CommentPageRequest;
import com.xhonell.common.domain.request.CommentSaveRequest;
import com.xhonell.common.domain.response.CommentResponse;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.server.mapper.CommentMapper;
import com.xhonell.server.mapper.FileMapper;
import com.xhonell.server.mapper.UserMapper;
import com.xhonell.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xhonell
 * @date 2026/3/26
 * @desc
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserMapper userMapper;
    private final FileMapper fileMapper;

    @Override
    public void publishComment(CommentSaveRequest request) {
        Comment comment = new Comment();
        comment.setUserId(RedisUserUtil.getUserId());
        comment.setContentId(request.getContentId());
        comment.setContent(request.getContent());
        comment.setStatus(1);
        save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        // 软删除，更新状态为0
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getId, id);
        updateWrapper.eq(Comment::getUserId, RedisUserUtil.getUserId());
        updateWrapper.set(Comment::getStatus, 0);
        update(updateWrapper);
    }

    @Override
    public PageInfo<CommentResponse> getCommentPage(CommentPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        // 根据内容ID查询
        if (request.getContentId() != null) {
            queryWrapper.eq(Comment::getContentId, request.getContentId());
        }

        // 根据用户ID查询
        if (request.getUserId() != null) {
            queryWrapper.eq(Comment::getUserId, request.getUserId());
        }

        // 根据状态查询（默认只查询正常的评论）
        queryWrapper.eq(Comment::getStatus, request.getStatus() != null ? request.getStatus() : 1);

        // 按创建时间倒序排列
        queryWrapper.orderByDesc(Comment::getCreateTime);

        List<Comment> comments = baseMapper.selectList(queryWrapper);
        PageInfo<Comment> pageInfo = PageUtils.toPageInfo(comments);

        // 批量查询用户信息
        List<Long> userIds = comments.stream().map(Comment::getUserId).distinct().collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return convertToCommentResponsePage(pageInfo);
        }

        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 批量查询头像文件信息
        List<Long> avatarIds = users.stream().map(User::getAvatarId).filter(id -> id != null).distinct().collect(Collectors.toList());
        Map<Long, File> fileMap = avatarIds.isEmpty() ? Map.of() : fileMapper.selectBatchIds(avatarIds).stream()
                .collect(Collectors.toMap(File::getId, file -> file));

        // 转换为 CommentResponse 并填充用户信息
        List<CommentResponse> responses = comments.stream().map(comment -> {
            CommentResponse response = new CommentResponse();
            response.setId(comment.getId());
            response.setUserId(comment.getUserId());
            response.setContentId(comment.getContentId());
            response.setContent(comment.getContent());
            response.setStatus(comment.getStatus());
            response.setCreateTime(comment.getCreateTime());
            response.setUpdateTime(comment.getUpdateTime());

            User user = userMap.get(comment.getUserId());
            if (user != null) {
                response.setUserName(user.getUsername());
                if (user.getAvatarId() != null) {
                    File file = fileMap.get(user.getAvatarId());
                    if (file != null) {
                        response.setAvatarUrl(file.getFilePathUrl());
                    }
                }
            }
            return response;
        }).collect(Collectors.toList());

        // 构建返回结果
        PageInfo<CommentResponse> resultPageInfo = new PageInfo<>();
        resultPageInfo.setPageNum(pageInfo.getPageNum());
        resultPageInfo.setPageSize(pageInfo.getPageSize());
        resultPageInfo.setSize(pageInfo.getSize());
        resultPageInfo.setTotal(pageInfo.getTotal());
        resultPageInfo.setPages(pageInfo.getPages());
        resultPageInfo.setList(responses);

        return resultPageInfo;
    }

    private PageInfo<CommentResponse> convertToCommentResponsePage(PageInfo<Comment> pageInfo) {
        PageInfo<CommentResponse> result = new PageInfo<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(pageInfo.getList().stream().map(comment -> {
            CommentResponse response = new CommentResponse();
            response.setId(comment.getId());
            response.setUserId(comment.getUserId());
            response.setContentId(comment.getContentId());
            response.setContent(comment.getContent());
            response.setStatus(comment.getStatus());
            response.setCreateTime(comment.getCreateTime());
            response.setUpdateTime(comment.getUpdateTime());
            return response;
        }).collect(Collectors.toList()));
        return result;
    }
}
