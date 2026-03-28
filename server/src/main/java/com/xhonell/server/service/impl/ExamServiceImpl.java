package com.xhonell.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.xhonell.common.domain.dto.RedisUser;
import com.xhonell.server.mapper.ExamAnswerMapper;
import com.xhonell.server.mapper.ExamOptionMapper;
import com.xhonell.server.mapper.ExamPaperMapper;
import com.xhonell.server.mapper.ExamQuestionMapper;
import com.xhonell.server.mapper.ExamRecordMapper;
import com.xhonell.server.service.ExamService;
import com.xhonell.common.domain.entity.ExamAnswer;
import com.xhonell.common.domain.entity.ExamOption;
import com.xhonell.common.domain.entity.ExamPaper;
import com.xhonell.common.domain.entity.ExamQuestion;
import com.xhonell.common.domain.entity.ExamRecord;
import com.xhonell.common.domain.request.ExamAnswerItemRequest;
import com.xhonell.common.domain.request.ExamSubmitRequest;
import com.xhonell.common.domain.response.ExamAnswerDetailResponse;
import com.xhonell.common.domain.response.ExamOptionResponse;
import com.xhonell.common.domain.response.ExamPaperResponse;
import com.xhonell.common.domain.response.ExamQuestionResponse;
import com.xhonell.common.domain.response.ExamRecordResponse;
import com.xhonell.common.domain.response.ExamWrongQuestionResponse;
import com.xhonell.common.utils.AssertUtil;
import com.xhonell.common.utils.PageUtils;
import com.xhonell.common.utils.RedisUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName ExamServiceImpl
 * description: 考试Service实现（用户端）
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamService {

    private final ExamPaperMapper examPaperMapper;
    private final ExamQuestionMapper examQuestionMapper;
    private final ExamOptionMapper examOptionMapper;
    private final ExamAnswerMapper examAnswerMapper;

    @Override
    public PageInfo<ExamPaperResponse> pageList(Integer page, Integer pageSize) {
        PageUtils.startPage(page, pageSize);

        LambdaQueryWrapper<ExamPaper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamPaper::getStatus, 1); // 只查询已发布的试卷
        queryWrapper.orderByDesc(ExamPaper::getId);

        List<ExamPaper> papers = examPaperMapper.selectList(queryWrapper);

        // 获取当前登录用户
        RedisUser redisUser = RedisUserUtil.get();
        Map<Long, Boolean> answeredMap;

        // 如果用户已登录，查询用户的考试记录
        if (redisUser != null) {
            LambdaQueryWrapper<ExamRecord> recordQuery = new LambdaQueryWrapper<>();
            recordQuery.eq(ExamRecord::getUserId, redisUser.getId());
            recordQuery.select(ExamRecord::getPaperId);
            List<ExamRecord> records = this.list(recordQuery);
            answeredMap = records.stream()
                    .collect(Collectors.toMap(ExamRecord::getPaperId, r -> true, (v1, v2) -> v1));
        } else {
            answeredMap = Map.of();
        }

        // 构建响应列表，并设置是否已答题
        List<ExamPaperResponse> responses = papers.stream().map(paper -> {
            ExamPaperResponse response = new ExamPaperResponse();
            BeanUtils.copyProperties(paper, response);
            response.setIsAnswered(answeredMap.getOrDefault(paper.getId(), false));
            return response;
        }).collect(Collectors.toList());

        return PageUtils.toPageInfo(responses);
    }

    @Override
    public ExamPaperResponse getPaperDetail(Long paperId) {
        ExamPaper paper = examPaperMapper.selectById(paperId);
        AssertUtil.isTrue(Objects.nonNull(paper), "试卷不存在");
        AssertUtil.isTrue(paper.getStatus() == 1, "试卷未发布");

        ExamPaperResponse response = new ExamPaperResponse();
        BeanUtils.copyProperties(paper, response);

        // 查询题目列表（不含答案）
        LambdaQueryWrapper<ExamQuestion> questionQuery = new LambdaQueryWrapper<>();
        questionQuery.eq(ExamQuestion::getPaperId, paperId);
        questionQuery.select(ExamQuestion::getId, ExamQuestion::getType, ExamQuestion::getContent,
                ExamQuestion::getScore, ExamQuestion::getSortOrder);
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
                optionQuery.select(ExamOption::getId, ExamOption::getContent, ExamOption::getOptionLabel, ExamOption::getSortOrder);
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
    public Long submitExam(ExamSubmitRequest request) {
        RedisUser redisUser = RedisUserUtil.get();
        AssertUtil.isTrue(Objects.nonNull(redisUser), "用户未登录");

        // 验证试卷
        ExamPaper paper = examPaperMapper.selectById(request.getPaperId());
        AssertUtil.isTrue(Objects.nonNull(paper), "试卷不存在");
        AssertUtil.isTrue(paper.getStatus() == 1, "试卷未发布");

        // 验证是否已经参加过考试
        LambdaQueryWrapper<ExamRecord> existQuery = new LambdaQueryWrapper<>();
        existQuery.eq(ExamRecord::getPaperId, request.getPaperId());
        existQuery.eq(ExamRecord::getUserId, redisUser.getId());
        ExamRecord existRecord = this.getOne(existQuery);
        AssertUtil.isTrue(Objects.isNull(existRecord), "您已经参加过该考试");

        // 验证答题列表
        AssertUtil.isTrue(!CollectionUtils.isEmpty(request.getAnswers()), "答题列表不能为空");

        // 查询题目信息
        List<Long> questionIds = request.getAnswers().stream()
                .map(ExamAnswerItemRequest::getQuestionId)
                .collect(Collectors.toList());
        LambdaQueryWrapper<ExamQuestion> questionQuery = new LambdaQueryWrapper<>();
        questionQuery.in(ExamQuestion::getId, questionIds);
        List<ExamQuestion> questions = examQuestionMapper.selectList(questionQuery);
        Map<Long, ExamQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(ExamQuestion::getId, q -> q));

        // 批量查询选项
        LambdaQueryWrapper<ExamOption> optionQuery = new LambdaQueryWrapper<>();
        optionQuery.in(ExamOption::getQuestionId, questionIds);
        optionQuery.orderByAsc(ExamOption::getSortOrder);
        List<ExamOption> options = examOptionMapper.selectList(optionQuery);
        Map<Long, List<ExamOption>> optionMap = options.stream()
                .collect(Collectors.groupingBy(ExamOption::getQuestionId));

        // 批量插入答题记录
        LocalDateTime now = LocalDateTime.now();
        int totalScore = 0;
        int correctCount = 0;

        for (ExamAnswerItemRequest answerRequest : request.getAnswers()) {
            ExamQuestion question = questionMap.get(answerRequest.getQuestionId());
            if (question == null) {
                continue;
            }

            // 判断答案是否正确
            boolean isCorrect = checkAnswer(question, answerRequest.getAnswer(), optionMap);
            int score = isCorrect ? question.getScore() : 0;

            ExamAnswer answer = new ExamAnswer();
            answer.setRecordId(0L); // 先设为0，稍后更新
            answer.setQuestionId(answerRequest.getQuestionId());
            answer.setUserAnswer(answerRequest.getAnswer());
            answer.setIsCorrect(isCorrect);
            answer.setScore(score);
            answer.setAnswerTime(now);
            examAnswerMapper.insert(answer);

            totalScore += score;
            if (isCorrect) {
                correctCount++;
            }
        }

        // 创建考试记录
        ExamRecord record = new ExamRecord();
        record.setPaperId(request.getPaperId());
        record.setUserId(redisUser.getId());
        record.setScore(totalScore);
        record.setAnswerCount(request.getAnswers().size());
        record.setCorrectCount(correctCount);
        record.setPassStatus(totalScore >= paper.getPassScore() ? 1 : 0);
        record.setStartTime(now);
        record.setSubmitTime(now);
        record.setDuration(0); // 前端计算并传递
        this.save(record);

        // 更新答题记录的recordId
        LambdaQueryWrapper<ExamAnswer> answerUpdateQuery = new LambdaQueryWrapper<>();
        answerUpdateQuery.eq(ExamAnswer::getRecordId, 0L);
        answerUpdateQuery.ge(ExamAnswer::getAnswerTime, now);
        List<ExamAnswer> answers = examAnswerMapper.selectList(answerUpdateQuery);
        answers.forEach(answer -> answer.setRecordId(record.getId()));
        if (!CollectionUtils.isEmpty(answers)) {
            answers.forEach(examAnswerMapper::updateById);
        }

        return record.getId();
    }

    @Override
    public PageInfo<ExamRecordResponse> getMyRecords(Integer page, Integer pageSize) {
        RedisUser redisUser = RedisUserUtil.get();
        AssertUtil.isTrue(Objects.nonNull(redisUser), "用户未登录");

        PageUtils.startPage(page, pageSize);

        LambdaQueryWrapper<ExamRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamRecord::getUserId, redisUser.getId());
        queryWrapper.orderByDesc(ExamRecord::getSubmitTime);

        List<ExamRecord> records = this.list(queryWrapper);

        // 查询试卷信息
        List<Long> paperIds = records.stream().map(ExamRecord::getPaperId).distinct().collect(Collectors.toList());
        Map<Long, ExamPaper> paperMap = examPaperMapper.selectBatchIds(paperIds).stream()
                .collect(Collectors.toMap(ExamPaper::getId, p -> p));

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
    public List<ExamAnswerDetailResponse> getRecordDetail(Long recordId) {
        RedisUser redisUser = RedisUserUtil.get();
        AssertUtil.isTrue(Objects.nonNull(redisUser), "用户未登录");

        ExamRecord record = this.getById(recordId);
        AssertUtil.isTrue(Objects.nonNull(record), "考试记录不存在");
        AssertUtil.isTrue(record.getUserId().equals(redisUser.getId()), "无权查看该考试记录");

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

    @Override
    public List<ExamWrongQuestionResponse> getWrongQuestions() {
        RedisUser redisUser = RedisUserUtil.get();
        AssertUtil.isTrue(Objects.nonNull(redisUser), "用户未登录");

        List<Map<String, Object>> list = ((ExamRecordMapper) this.getBaseMapper()).getWrongQuestionList(redisUser.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return list.stream().map(map -> {
            ExamWrongQuestionResponse response = new ExamWrongQuestionResponse();
            response.setQuestionId(((Number) map.get("question_id")).longValue());
            response.setType(((Number) map.get("type")).intValue());
            response.setTypeName(getQuestionTypeName(response.getType()));
            response.setContent((String) map.get("content"));
            response.setCorrectAnswer((String) map.get("answer"));
            response.setAnalysis((String) map.get("analysis"));
            response.setWrongCount(((Number) map.get("wrong_count")).intValue());
            response.setLastWrongTime(map.get("last_wrong_time").toString());

            // 查询选项
            LambdaQueryWrapper<ExamOption> optionQuery = new LambdaQueryWrapper<>();
            optionQuery.eq(ExamOption::getQuestionId, response.getQuestionId());
            optionQuery.orderByAsc(ExamOption::getSortOrder);
            List<ExamOption> options = examOptionMapper.selectList(optionQuery);
            if (!CollectionUtils.isEmpty(options)) {
                List<ExamOptionResponse> optionResponses = options.stream().map(option -> {
                    ExamOptionResponse optionResponse = new ExamOptionResponse();
                    BeanUtils.copyProperties(option, optionResponse);
                    return optionResponse;
                }).collect(Collectors.toList());
                response.setOptions(optionResponses);
            }

            return response;
        }).collect(Collectors.toList());
    }

    private boolean checkAnswer(ExamQuestion question, String userAnswer, Map<Long, List<ExamOption>> optionMap) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            return false;
        }

        String correctAnswer = question.getAnswer();
        if (correctAnswer == null) {
            return false;
        }

        // 根据题目类型判断
        return switch (question.getType()) {
            case 1, 2 -> { // 单选题、多选题
                // 对于选择题，比对选项标签
                List<ExamOption> options = optionMap.get(question.getId());
                if (CollectionUtils.isEmpty(options)) {
                    yield false;
                }
                String[] userLabels = userAnswer.split(",");
                String[] correctLabels = correctAnswer.split(",");
                if (userLabels.length != correctLabels.length) {
                    yield false;
                }
                boolean allMatch = true;
                for (String userLabel : userLabels) {
                    boolean found = false;
                    for (String correctLabel : correctLabels) {
                        if (userLabel.trim().equalsIgnoreCase(correctLabel.trim())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        allMatch = false;
                        break;
                    }
                }
                yield allMatch;
            }
            case 3 -> { // 判断题
                yield userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
            }
            case 4, 5 -> { // 填空题、简答题
                // 简单匹配，可以后续优化为模糊匹配
                yield userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
            }
            default -> false;
        };
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