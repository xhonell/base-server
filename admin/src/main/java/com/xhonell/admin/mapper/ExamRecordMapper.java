package com.xhonell.admin.mapper;

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
 * description: 考试记录Mapper
 * author: xhonell
 * create: 2026年3月28日
 * Version 1.0
 **/
@Mapper
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {

    /**
     * 获取试卷排名列表
     *
     * @param paperId 试卷ID
     * @return 排名列表
     */
    @Select("SELECT er.id, er.user_id, u.username, er.score, er.duration, er.submit_time, " +
            "RANK() OVER (ORDER BY er.score DESC, er.duration ASC) as rank " +
            "FROM pe_exam_record er " +
            "LEFT JOIN pe_user u ON er.user_id = u.id " +
            "WHERE er.paper_id = #{paperId} " +
            "ORDER BY er.score DESC, er.duration ASC")
    List<Map<String, Object>> getRankingList(@Param("paperId") Long paperId);
}