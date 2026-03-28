package com.xhonell.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.admin.mapper.ExamAnswerMapper;
import com.xhonell.admin.mapper.ExamOptionMapper;
import com.xhonell.admin.mapper.ExamPaperMapper;
import com.xhonell.admin.mapper.ExamQuestionMapper;
import com.xhonell.admin.mapper.ExamRecordMapper;
import com.xhonell.admin.service.ExamRecordService;
import com.xhonell.common.domain.entity.ExamAnswer;
import com.xhonell.common.domain.entity.ExamOption;
import com.xhonell.common.domain.entity.ExamPaper;
import com.xhonell.common.domain.entity.ExamQuestion;
import com.xhonell.common.domain.entity.ExamRecord;
import com.xhonell.common.domain.response.ExamAnswerDetailResponse;
import com.xhonell.common.domain.response.ExamOptionResponse;
import com.xhonell.common.domain.response.ExamRankingResponse;
import com.xhonell.common.domain.response.ExamRecordResponse;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName ExamRecordServiceImpl
 * description: 考试记录Service实现
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamRecordService {

    private final ExamAnswerMapper examAnswerMapper;
    private final ExamQuestionMapper examQuestionMapper;
    private final ExamOptionMapper examOptionMapper;
    private final ExamPaperMapper examPaperMapper;

    @Override
    public PageInfo<ExamRecordResponse> pageList(Long paperId, Integer page, Integer pageSize) {
        PageUtils.startPage(page, pageSize);

        LambdaQueryWrapper<ExamRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(paperId), ExamRecord::getPaperId, paperId);
        queryWrapper.orderByDesc(ExamRecord::getSubmitTime);

        List<ExamRecord> records = this.list(queryWrapper);

        // 查询试卷信息
        List<Long> paperIds = records.stream().map(ExamRecord::getPaperId).distinct().collect(Collectors.toList());
        Map<Long, ExamPaper> paperMap;
        if (!CollectionUtils.isEmpty(paperIds)) {
            paperMap = examPaperMapper.selectBatchIds(paperIds).stream()
                    .collect(Collectors.toMap(ExamPaper::getId, p -> p));
        } else {
            paperMap = Map.of();
        }

        List<ExamRecordResponse> responses = records.stream().map(record -> {
            ExamRecordResponse response = new ExamRecordResponse();
            BeanUtils.copyProperties(record, response);
            ExamPaper paper = paperMap.get(record.getPaperId());
            if (paper != null) {
                response.setPaperTitle(paper.getTitle());
            }
            return response;
        }).collect(Collectors.toList());

        return PageUtils.toPageInfo(responses);
    }

    @Override
    public List<ExamRankingResponse> getRanking(Long paperId) {
        AssertUtil.isTrue(Objects.nonNull(paperId), "试卷ID不能为空");

        List<Map<String, Object>> list = ((ExamRecordMapper) this.getBaseMapper()).getRankingList(paperId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return list.stream().map(map -> {
            ExamRankingResponse response = new ExamRankingResponse();
            response.setRank(((Number) map.get("rank")).intValue());
            response.setUserId(((Number) map.get("user_id")).longValue());
            response.setUsername((String) map.get("username"));
            response.setScore(((Number) map.get("score")).intValue());
            response.setDuration(((Number) map.get("duration")).intValue());
            response.setSubmitTime(map.get("submit_time").toString());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ExamAnswerDetailResponse> getAnswerDetail(Long recordId) {
        AssertUtil.isTrue(Objects.nonNull(recordId), "考试记录ID不能为空");

        ExamRecord record = this.getById(recordId);
        AssertUtil.isTrue(Objects.nonNull(record), "考试记录不存在");

        // 查询答题记录
        LambdaQueryWrapper<ExamAnswer> answerQuery = new LambdaQueryWrapper<>();
        answerQuery.eq(ExamAnswer::getRecordId, recordId);
        answerQuery.orderByAsc(ExamAnswer::getId);
        List<ExamAnswer> answers = examAnswerMapper.selectList(answerQuery);

        if (CollectionUtils.isEmpty(answers)) {
            return List.of();
        }

        // 查询题目信息
        List<Long> questionIds = answers.stream().map(ExamAnswer::getQuestionId).collect(Collectors.toList());
        LambdaQueryWrapper<ExamQuestion> questionQuery = new LambdaQueryWrapper<>();
        questionQuery.in(ExamQuestion::getId, questionIds);
        List<ExamQuestion> questions = examQuestionMapper.selectList(questionQuery);
        Map<Long, ExamQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(ExamQuestion::getId, q -> q));

        // 查询选项信息
        LambdaQueryWrapper<ExamOption> optionQuery = new LambdaQueryWrapper<>();
        optionQuery.in(ExamOption::getQuestionId, questionIds);
        optionQuery.orderByAsc(ExamOption::getSortOrder);
        List<ExamOption> options = examOptionMapper.selectList(optionQuery);
        Map<Long, List<ExamOption>> optionMap = options.stream()
                .collect(Collectors.groupingBy(ExamOption::getQuestionId));

        return answers.stream().map(answer -> {
            ExamAnswerDetailResponse response = new ExamAnswerDetailResponse();
            response.setId(answer.getId());
            response.setQuestionId(answer.getQuestionId());
            response.setUserAnswer(answer.getUserAnswer());
            response.setIsCorrect(answer.getIsCorrect());
            response.setScore(answer.getScore());

            ExamQuestion question = questionMap.get(answer.getQuestionId());
            if (question != null) {
                response.setType(question.getType());
                response.setContent(question.getContent());
                response.setCorrectAnswer(question.getAnswer());
                response.setAnalysis(question.getAnalysis());

                // 查询选项
                List<ExamOption> questionOptions = optionMap.get(question.getId());
                if (!CollectionUtils.isEmpty(questionOptions)) {
                    List<ExamOptionResponse> optionResponses = questionOptions.stream().map(option -> {
                        ExamOptionResponse optionResponse = new ExamOptionResponse();
                        BeanUtils.copyProperties(option, optionResponse);
                        return optionResponse;
                    }).collect(Collectors.toList());
                    response.setOptions(optionResponses);
                }
            }

            return response;
        }).collect(Collectors.toList());
    }
}