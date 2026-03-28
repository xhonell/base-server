package com.xhonell.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.entity.Comment;
import com.xhonell.common.domain.entity.File;
import com.xhonell.common.domain.entity.Topic;
import com.xhonell.common.domain.entity.User;
import com.xhonell.common.domain.request.TopicCommentPageRequest;
import com.xhonell.common.domain.request.TopicCommentRequest;
import com.xhonell.common.domain.request.TopicPageRequest;
import com.xhonell.common.domain.request.TopicSaveRequest;
import com.xhonell.common.domain.response.TopicCommentResponse;
import com.xhonell.common.domain.response.TopicResponse;
import com.xhonell.common.exception.BizException;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.common.utils.RandomUtil;
import com.xhonell.common.utils.RedisUserUtil;
import com.xhonell.server.mapper.CommentMapper;
import com.xhonell.server.mapper.FileMapper;
import com.xhonell.server.mapper.TopicMapper;
import com.xhonell.server.mapper.UserMapper;
import com.xhonell.server.service.TopicService;
import com.xhonell.server.utils.IpRegionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName TopicServiceImpl
 * description: 话题服务实现类
 * author: xhonell
 * create: 2026/3/28
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    private final UserMapper userMapper;
    private final FileMapper fileMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long publishTopic(TopicSaveRequest request) {
        Topic topic = new Topic();
        topic.setUserId(RedisUserUtil.getUserId());
        topic.setTitle(request.getTitle());
        topic.setContent(request.getContent());
        topic.setCategory(request.getCategory());
        topic.setViewCount(0);
        topic.setId(Long.valueOf(RandomUtil.randomNumber(10)))  ;
        topic.setReplyCount(0);
        topic.setStatus(1);
        save(topic);
        return topic.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TopicResponse getTopicDetail(Long id) {
        // 查询话题
        Topic topic = baseMapper.selectById(id);
        if (topic == null || topic.getStatus() == 0) {
            throw new BizException("话题不存在或已被删除");
        }

        // 查看数+1
        LambdaUpdateWrapper<Topic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Topic::getId, id);
        updateWrapper.setSql("view_count = view_count + 1");
        update(updateWrapper);
        topic.setViewCount(topic.getViewCount() + 1);

        // 查询用户信息
        User user = userMapper.selectById(topic.getUserId());
        String userName = user != null ? user.getUsername() : null;
        String avatarUrl = null;
        String location = null;
        if (user != null) {
            if (user.getAvatarId() != null) {
                File file = fileMapper.selectById(user.getAvatarId());
                if (file != null) {
                    avatarUrl = file.getFilePathUrl();
                }
            }
        }

        // 构建返回结果
        TopicResponse response = new TopicResponse();
        response.setId(topic.getId());
        response.setUserId(topic.getUserId());
        response.setUserName(userName);
        response.setAvatarUrl(avatarUrl);
        response.setLocation(location);
        response.setTitle(topic.getTitle());
        response.setContent(topic.getContent());
        response.setCategory(topic.getCategory());
        response.setViewCount(topic.getViewCount());
        response.setReplyCount(topic.getReplyCount());
        response.setStatus(topic.getStatus());
        response.setCreateTime(topic.getCreateTime());
        response.setUpdateTime(topic.getUpdateTime());

        return response;
    }

    @Override
    public PageInfo<TopicResponse> getTopicPage(TopicPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());

        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();

        // 根据用户ID查询
        if (request.getUserId() != null) {
            queryWrapper.eq("user_id", request.getUserId());
        }

        // 根据分类查询
        if (request.getCategory() != null) {
            queryWrapper.eq("category", request.getCategory());
        }

        // 根据状态查询（默认只查询正常的话题）
        queryWrapper.eq("status", request.getStatus() != null ? request.getStatus() : 1);

        // 动态排序
        if (StringUtils.hasText(request.getOrderBy())) {
            queryWrapper.orderBy(true, Objects.equals(request.getOrderType(), "ASC"), request.getOrderBy());
        } else {
            // 默认按创建时间倒序排列
            queryWrapper.orderByDesc("id");
        }

        List<Topic> topics = baseMapper.selectList(queryWrapper);
        PageInfo<Topic> pageInfo = PageUtils.toPageInfo(topics);

        // 批量查询用户信息
        List<Long> userIds = topics.stream().map(Topic::getUserId).distinct().collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return convertToTopicResponsePage(pageInfo);
        }

        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 批量查询头像文件信息
        List<Long> avatarIds = users.stream().map(User::getAvatarId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, File> fileMap = avatarIds.isEmpty() ? Map.of() : fileMapper.selectBatchIds(avatarIds).stream()
                .collect(Collectors.toMap(File::getId, file -> file));

        // 转换为 TopicResponse 并填充用户信息
        List<TopicResponse> responses = topics.stream().map(topic -> {
            TopicResponse response = new TopicResponse();
            response.setId(topic.getId());
            response.setUserId(topic.getUserId());
            response.setTitle(topic.getTitle());
            response.setContent(topic.getContent());
            response.setCategory(topic.getCategory());
            response.setViewCount(topic.getViewCount());
            response.setReplyCount(topic.getReplyCount());
            response.setStatus(topic.getStatus());
            response.setCreateTime(topic.getCreateTime());
            response.setUpdateTime(topic.getUpdateTime());

            User user = userMap.get(topic.getUserId());
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
        PageInfo<TopicResponse> resultPageInfo = new PageInfo<>();
        resultPageInfo.setPageNum(pageInfo.getPageNum());
        resultPageInfo.setPageSize(pageInfo.getPageSize());
        resultPageInfo.setSize(pageInfo.getSize());
        resultPageInfo.setTotal(pageInfo.getTotal());
        resultPageInfo.setPages(pageInfo.getPages());
        resultPageInfo.setList(responses);

        return resultPageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishTopicComment(TopicCommentRequest request) {
        // 查询话题是否存在
        Topic topic = baseMapper.selectById(request.getTopicId());
        if (topic == null || topic.getStatus() == 0) {
            throw new BizException("话题不存在或已被删除");
        }

        Long currentUserId = RedisUserUtil.getUserId();

        // 如果是回复评论
        if (request.getParentId() != null) {
            Comment parentComment = commentMapper.selectById(request.getParentId());
            if (parentComment == null || parentComment.getStatus() == 0) {
                throw new BizException("父评论不存在或已被删除");
            }
            // 父评论回复数+1
            LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Comment::getId, request.getParentId());
            updateWrapper.setSql("reply_count = reply_count + 1");
            commentMapper.update(null, updateWrapper);
        }

        // 创建评论
        Comment comment = new Comment();
        comment.setUserId(currentUserId);
        comment.setContentId(request.getTopicId());
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        comment.setStatus(1);
        comment.setReplyCount(0);
        commentMapper.insert(comment);

        // 话题回复数+1
        LambdaUpdateWrapper<Topic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Topic::getId, request.getTopicId());
        updateWrapper.setSql("reply_count = reply_count + 1");
        update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTopicComment(Long id) {
        // 查询评论
        Comment comment = commentMapper.selectById(id);
        if (comment == null || comment.getStatus() == 0) {
            throw new BizException("评论不存在或已被删除");
        }

        Long currentUserId = RedisUserUtil.getUserId();
        if (!comment.getUserId().equals(currentUserId)) {
            throw new BizException("只能删除自己的评论");
        }

        // 软删除评论
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getId, id);
        updateWrapper.set(Comment::getStatus, 0);
        commentMapper.update(null, updateWrapper);

        // 如果是回复，父评论回复数-1
        if (comment.getParentId() != null) {
            LambdaUpdateWrapper<Comment> parentUpdateWrapper = new LambdaUpdateWrapper<>();
            parentUpdateWrapper.eq(Comment::getId, comment.getParentId());
            parentUpdateWrapper.setSql("reply_count = reply_count - 1");
            commentMapper.update(null, parentUpdateWrapper);
        }

        // 话题回复数-1
        LambdaUpdateWrapper<Topic> topicUpdateWrapper = new LambdaUpdateWrapper<>();
        topicUpdateWrapper.eq(Topic::getId, comment.getContentId());
        topicUpdateWrapper.setSql("reply_count = reply_count - 1");
        update(topicUpdateWrapper);
    }

    @Override
    public PageInfo<TopicCommentResponse> getTopicCommentPage(TopicCommentPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());

        // 查询一级评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getContentId, request.getTopicId());
        queryWrapper.eq(Comment::getStatus, 1);
        queryWrapper.isNull(Comment::getParentId); // 只查询一级评论
        queryWrapper.orderByDesc(Comment::getCreateTime);

        List<Comment> parentComments = commentMapper.selectList(queryWrapper);
        PageInfo<Comment> pageInfo = PageUtils.toPageInfo(parentComments);

        // 查询所有二级评论（所有父评论的子评论）
        List<Long> parentCommentIds = parentComments.stream().map(Comment::getId).collect(Collectors.toList());
        List<Comment> allChildComments;
        if (!parentCommentIds.isEmpty()) {
            LambdaQueryWrapper<Comment> childQueryWrapper = new LambdaQueryWrapper<>();
            childQueryWrapper.in(Comment::getParentId, parentCommentIds);
            childQueryWrapper.eq(Comment::getStatus, 1);
            childQueryWrapper.orderByAsc(Comment::getCreateTime);
            allChildComments = commentMapper.selectList(childQueryWrapper);
        } else {
            allChildComments = List.of();
        }

        // 合并所有评论以批量查询用户信息
        List<Comment> allComments = new java.util.ArrayList<>(parentComments);
        allComments.addAll(allChildComments);

        // 批量查询用户信息
        List<Long> userIds = allComments.stream().map(Comment::getUserId).distinct().collect(Collectors.toList());
        Map<Long, User> userMap;
        if (!userIds.isEmpty()) {
            List<User> users = userMapper.selectBatchIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        } else {
            userMap = Map.of();
        }

        // 批量查询头像文件信息
        Map<Long, File> fileMap;
        if (!userIds.isEmpty()) {
            List<Long> avatarIds = userMap.values().stream()
                    .map(User::getAvatarId)
                    .filter(id -> id != null)
                    .distinct()
                    .collect(Collectors.toList());
            if (!avatarIds.isEmpty()) {
                fileMap = fileMapper.selectBatchIds(avatarIds).stream()
                        .collect(Collectors.toMap(File::getId, file -> file));
            } else {
                fileMap = Map.of();
            }
        } else {
            fileMap = Map.of();
        }

        // 构建父评论的响应列表
        List<TopicCommentResponse> responses = parentComments.stream().map(parentComment -> {
            TopicCommentResponse parentResponse = buildCommentResponse(parentComment, userMap, fileMap);

            // 查询并设置子评论
            List<Comment> childComments = allChildComments.stream()
                    .filter(child -> child.getParentId().equals(parentComment.getId()))
                    .toList();

            if (!childComments.isEmpty()) {
                List<TopicCommentResponse> childResponses = childComments.stream()
                        .map(child -> buildCommentResponse(child, userMap, fileMap))
                        .collect(Collectors.toList());
                parentResponse.setChildren(childResponses);
            }

            return parentResponse;
        }).collect(Collectors.toList());

        // 构建返回结果
        PageInfo<TopicCommentResponse> resultPageInfo = new PageInfo<>();
        resultPageInfo.setPageNum(pageInfo.getPageNum());
        resultPageInfo.setPageSize(pageInfo.getPageSize());
        resultPageInfo.setSize(pageInfo.getSize());
        resultPageInfo.setTotal(pageInfo.getTotal());
        resultPageInfo.setPages(pageInfo.getPages());
        resultPageInfo.setList(responses);

        return resultPageInfo;
    }

    /**
     * 构建评论响应对象
     */
    private TopicCommentResponse buildCommentResponse(Comment comment, Map<Long, User> userMap, Map<Long, File> fileMap) {
        TopicCommentResponse response = new TopicCommentResponse();
        response.setId(comment.getId());
        response.setUserId(comment.getUserId());
        response.setTopicId(comment.getContentId());
        response.setParentId(comment.getParentId());
        response.setReplyCount(comment.getReplyCount());
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
    }

    private PageInfo<TopicResponse> convertToTopicResponsePage(PageInfo<Topic> pageInfo) {
        PageInfo<TopicResponse> result = new PageInfo<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(pageInfo.getList().stream().map(topic -> {
            TopicResponse response = new TopicResponse();
            response.setId(topic.getId());
            response.setUserId(topic.getUserId());
            response.setTitle(topic.getTitle());
            response.setContent(topic.getContent());
            response.setCategory(topic.getCategory());
            response.setViewCount(topic.getViewCount());
            response.setReplyCount(topic.getReplyCount());
            response.setStatus(topic.getStatus());
            response.setCreateTime(topic.getCreateTime());
            response.setUpdateTime(topic.getUpdateTime());

            return response;
        }).collect(Collectors.toList()));
        return result;
    }
}