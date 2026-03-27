package com.xhonell.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * program: BaseServer
 * ClassName DailyPointsRecord
 * description: 每日积分记录表（统计用户每天各类型获得的积分）
 * author: xhonell
 * create: 2026年3月27日
 * Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("pe_daily_points_record")
public class DailyPointsRecord {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 来源类型（1文章学习 2视频学习 3评论 4点赞 5收藏）
     */
    private Integer sourceType;

    /**
     * 今日获得的积分数
     */
    private Integer points;

    /**
     * 今日操作次数
     */
    private Integer count;

    /**
     * 日期
     */
    private LocalDate recordDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}