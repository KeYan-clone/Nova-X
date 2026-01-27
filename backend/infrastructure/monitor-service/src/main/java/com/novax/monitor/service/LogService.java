package com.novax.monitor.service;

import com.novax.monitor.dto.LogQueryDTO;
import com.novax.monitor.vo.LogRecordVO;
import com.novax.common.core.result.PageResult;

import java.util.List;

/**
 * 日志服务接口
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
public interface LogService {

    /**
     * 查询日志
     *
     * @param queryDTO 查询条件
     * @return 日志列表
     */
    PageResult<LogRecordVO> queryLogs(LogQueryDTO queryDTO);

    /**
     * 根据TraceId查询链路日志
     *
     * @param traceId TraceId
     * @return 日志列表
     */
    List<LogRecordVO> queryByTraceId(String traceId);

    /**
     * 统计错误日志数量
     *
     * @param serviceName 服务名称
     * @param hours 小时数
     * @return 错误数量
     */
    Long countErrors(String serviceName, Integer hours);
}
