package com.xhonell.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhonell.common.domain.entity.PointsRecord;
import com.xhonell.common.domain.request.PointsChangeRequest;
import com.xhonell.common.domain.response.PointsChangeResponse;

/**
 * @author xhonell
 * @date 2026/3/27
 * @desc
 */
public interface PointsRecordService extends IService<PointsRecord> {

    /**
     * 增加用户积分
     *
     * @param request 积分变动请求
     * @return 积分变动响应
     */
    PointsChangeResponse addPoints(PointsChangeRequest request);
}
