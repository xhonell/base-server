package com.xhonell.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhonell.common.domain.entity.ExamRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * program: BaseServer
 * ClassName ExamRecordMapper
 * description: 考试记录Mapper（用户端）
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Mapper
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {

    /**
     * 获取用户的错题统计
     *
     * @param userId 用户ID
     * @return 错题统计列表
     */
    @Select("SELECT ea.question_id, eq.type, eq.content, eq.answer, eq.analysis, " +
            "COUNT(*) as wrong_count, MAX(ea.answer_time) as last_wrong_time " +
            "FROM pe_exam_answer ea " +
            "LEFT JOIN pe_exam_question eq ON ea.question_id = eq.id " +
            "LEFT JOIN pe_exam_record er ON ea.record_id = er.id " +
            "WHERE er.user_id = #{userId} AND ea.is_correct = 0 " +
            "GROUP BY ea.question_id, eq.type, eq.content, eq.answer, eq.analysis " +
            "ORDER BY wrong_count DESC")
    List<Map<String, Object>> getWrongQuestionList(@Param("userId") Long userId);
}