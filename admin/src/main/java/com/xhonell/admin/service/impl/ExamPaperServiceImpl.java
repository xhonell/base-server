package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.ExamOptionMapper;
import com.xhonell.admin.mapper.ExamPaperMapper;
import com.xhonell.admin.mapper.ExamQuestionMapper;
import com.xhonell.admin.service.ExamPaperService;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.common.domain.entity.ExamOption;
import com.xhonell.common.domain.entity.ExamPaper;
import com.xhonell.common.domain.entity.ExamQuestion;
import com.xhonell.common.domain.request.ExamOptionSaveRequest;
import com.xhonell.common.domain.request.ExamPaperPageRequest;
import com.xhonell.common.domain.request.ExamPaperSaveRequest;
import com.xhonell.common.domain.request.ExamQuestionSaveRequest;
import com.xhonell.common.domain.response.ExamOptionResponse;
import com.xhonell.common.domain.response.ExamPaperResponse;
import com.xhonell.common.domain.response.ExamQuestionResponse;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.exception.BizException;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.common.utils.RedisUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName ExamPaperServiceImpl
 * description: 试卷Service实现
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ExamPaperServiceImpl extends ServiceImpl<ExamPaperMapper, ExamPaper> implements ExamPaperService {

    private final ExamQuestionMapper examQuestionMapper;
    private final ExamOptionMapper examOptionMapper;

    @Override
    public PageInfo<ExamPaperResponse> pageList(ExamPaperPageRequest request) {
        PageUtils.startPage(request.getPage(), request.getPageSize());

        LambdaQueryWrapper<ExamPaper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(request.getTitle()), ExamPaper::getTitle, request.getTitle());
        queryWrapper.eq(Objects.nonNull(request.getStatus()), ExamPaper::getStatus, request.getStatus());
        queryWrapper.orderByDesc(ExamPaper::getId);

        List<ExamPaper> papers = this.list(queryWrapper);
        List<ExamPaperResponse> responses = papers.stream().map(paper -> {
            ExamPaperResponse response = new ExamPaperResponse();
            BeanUtils.copyProperties(paper, response);
            return response;
        }).collect(Collectors.toList());

        return PageUtils.toPageInfo(responses);
    }

    @Override
    public ExamPaperResponse getDetail(Long paperId) {
        ExamPaper paper = this.getById(paperId);
        AssertUtil.isTrue(Objects.nonNull(paper), "试卷不存在");

        ExamPaperResponse response = new ExamPaperResponse();
        BeanUtils.copyProperties(paper, response);

        // 查询题目列表
        LambdaQueryWrapper<ExamQuestion> questionQuery = new LambdaQueryWrapper<>();
        questionQuery.eq(ExamQuestion::getPaperId, paperId);
        questionQuery.orderByAsc(ExamQuestion::getSortOrder);
        List<ExamQuestion> questions = examQuestionMapper.selectList(questionQuery);

        if (!CollectionUtils.isEmpty(questions)) {
            List<ExamQuestionResponse> questionResponses = questions.stream().map(question -> {
                ExamQuestionResponse questionResponse = new ExamQuestionResponse();
                BeanUtils.copyProperties(question, questionResponse);
                questionResponse.setTypeName(getQuestionTypeName(question.getType()));

                // 查询选项列表
                LambdaQueryWrapper<ExamOption> optionQuery = new LambdaQueryWrapper<>();
                optionQuery.eq(ExamOption::getQuestionId, question.getId());
                optionQuery.orderByAsc(ExamOption::getSortOrder);
                List<ExamOption> options = examOptionMapper.selectList(optionQuery);

                if (!CollectionUtils.isEmpty(options)) {
                    List<ExamOptionResponse> optionResponses = options.stream().map(option -> {
                        ExamOptionResponse optionResponse = new ExamOptionResponse();
                        BeanUtils.copyProperties(option, optionResponse);
                        return optionResponse;
                    }).collect(Collectors.toList());
                    questionResponse.setOptions(optionResponses);
                }

                return questionResponse;
            }).collect(Collectors.toList());
            response.setQuestions(questionResponses);
        }

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePaper(ExamPaperSaveRequest request) {
        RedisUser redisUser = RedisUserUtil.get();
        AssertUtil.isTrue(Objects.nonNull(redisUser), "用户未登录");

        // 验证题目列表
        AssertUtil.isTrue(!CollectionUtils.isEmpty(request.getQuestions()), "试卷至少需要一道题目");

        // 计算总分和题目数量
        int totalScore = 0;
        for (ExamQuestionSaveRequest questionRequest : request.getQuestions()) {
            totalScore += questionRequest.getScore();
        }

        ExamPaper paper;
        if (request.getId() != null) {
            // 更新试卷
            paper = this.getById(request.getId());
            AssertUtil.isTrue(Objects.nonNull(paper), "试卷不存在");
            AssertUtil.isTrue(paper.getStatus() == 0, "已发布的试卷不能修改");

            paper.setTitle(request.getTitle());
            paper.setDescription(request.getDescription());
            paper.setDuration(request.getDuration());
            paper.setPassScore(request.getPassScore());
            paper.setTotalScore(totalScore);
            paper.setQuestionCount(request.getQuestions().size());
            paper.setUpdateTime(LocalDateTime.now());
            this.updateById(paper);

            // 删除原有题目和选项
            LambdaQueryWrapper<ExamQuestion> questionQuery = new LambdaQueryWrapper<>();
            questionQuery.eq(ExamQuestion::getPaperId, paper.getId());
            List<ExamQuestion> oldQuestions = examQuestionMapper.selectList(questionQuery);
            if (!CollectionUtils.isEmpty(oldQuestions)) {
                List<Long> questionIds = oldQuestions.stream().map(ExamQuestion::getId).collect(Collectors.toList());
                LambdaQueryWrapper<ExamOption> optionQuery = new LambdaQueryWrapper<>();
                optionQuery.in(ExamOption::getQuestionId, questionIds);
                examOptionMapper.delete(optionQuery);
                examQuestionMapper.deleteBatchIds(questionIds);
            }
        } else {
            // 新增试卷
            paper = new ExamPaper();
            paper.setTitle(request.getTitle());
            paper.setDescription(request.getDescription());
            paper.setDuration(request.getDuration());
            paper.setPassScore(request.getPassScore());
            paper.setTotalScore(totalScore);
            paper.setQuestionCount(request.getQuestions().size());
            paper.setStatus(0);
            paper.setCreatorId(redisUser.getId());
            paper.setCreateTime(LocalDateTime.now());
            paper.setUpdateTime(LocalDateTime.now());
            this.save(paper);
        }

        // 保存题目和选项
        for (int i = 0; i < request.getQuestions().size(); i++) {
            ExamQuestionSaveRequest questionRequest = request.getQuestions().get(i);
            ExamQuestion question = new ExamQuestion();
            question.setPaperId(paper.getId());
            question.setType(questionRequest.getType());
            question.setContent(questionRequest.getContent());
            question.setScore(questionRequest.getScore());
            question.setSortOrder(questionRequest.getSortOrder() != null ? questionRequest.getSortOrder() : i + 1);
            question.setAnswer(questionRequest.getAnswer());
            question.setAnalysis(questionRequest.getAnalysis());
            question.setCreateTime(LocalDateTime.now());
            question.setUpdateTime(LocalDateTime.now());
            examQuestionMapper.insert(question);

            // 保存选项（单选题和多选题）
            if ((question.getType() == 1 || question.getType() == 2) && !CollectionUtils.isEmpty(questionRequest.getOptions())) {
                for (int j = 0; j < questionRequest.getOptions().size(); j++) {
                    ExamOptionSaveRequest optionRequest = questionRequest.getOptions().get(j);
                    ExamOption option = new ExamOption();
                    option.setQuestionId(question.getId());
                    option.setContent(optionRequest.getContent());
                    option.setOptionLabel(optionRequest.getOptionLabel());
                    option.setIsCorrect(optionRequest.getIsCorrect());
                    option.setSortOrder(optionRequest.getSortOrder() != null ? optionRequest.getSortOrder() : j + 1);
                    examOptionMapper.insert(option);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaper(Long paperId) {
        ExamPaper paper = this.getById(paperId);
        AssertUtil.isTrue(Objects.nonNull(paper), "试卷不存在");
        AssertUtil.isTrue(paper.getStatus() == 0, "已发布的试卷不能删除");

        // 删除题目和选项
        LambdaQueryWrapper<ExamQuestion> questionQuery = new LambdaQueryWrapper<>();
        questionQuery.eq(ExamQuestion::getPaperId, paperId);
        List<ExamQuestion> questions = examQuestionMapper.selectList(questionQuery);
        if (!CollectionUtils.isEmpty(questions)) {
            List<Long> questionIds = questions.stream().map(ExamQuestion::getId).collect(Collectors.toList());
            LambdaQueryWrapper<ExamOption> optionQuery = new LambdaQueryWrapper<>();
            optionQuery.in(ExamOption::getQuestionId, questionIds);
            examOptionMapper.delete(optionQuery);
            examQuestionMapper.deleteBatchIds(questionIds);
        }

        this.removeById(paperId);
    }

    @Override
    public void updateStatus(Long paperId, Integer status) {
        ExamPaper paper = this.getById(paperId);
        AssertUtil.isTrue(Objects.nonNull(paper), "试卷不存在");
        AssertUtil.isTrue(paper.getStatus() != status, "状态无需更新");

        // 发布时验证题目数量
        if (status == 1) {
            LambdaQueryWrapper<ExamQuestion> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ExamQuestion::getPaperId, paperId);
            long count = examQuestionMapper.selectCount(queryWrapper);
            AssertUtil.isTrue(count > 0, "试卷没有题目，不能发布");
        }

        paper.setStatus(status);
        paper.setUpdateTime(LocalDateTime.now());
        this.updateById(paper);
    }

    private String getQuestionTypeName(Integer type) {
        return switch (type) {
            case 1 -> "单选题";
            case 2 -> "多选题";
            case 3 -> "判断题";
            case 4 -> "填空题";
            case 5 -> "简答题";
            default -> "未知";
        };
    }
}