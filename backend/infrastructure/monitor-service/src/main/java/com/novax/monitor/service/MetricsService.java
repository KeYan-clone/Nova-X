package com.novax.monitor.service;

import com.novax.monitor.vo.ServiceMetricsVO;

import java.util.List;
import java.util.Map;

/**
 * 指标服务接口
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
public interface MetricsService {

    /**
     * 获取服务指标
     *
     * @param serviceName 服务名称
     * @return 服务指标
     */
    ServiceMetricsVO getServiceMetrics(String serviceName);

    /**
     * 获取所有服务指标
     *
     * @return 服务指标列表
     */
    List<ServiceMetricsVO> getAllServicesMetrics();

    /**
     * 获取自定义指标
     *
     * @param serviceName 服务名称
     * @param metricName 指标名称
     * @return 指标值
     */
    Map<String, Object> getCustomMetrics(String serviceName, String metricName);

    /**
     * 查询指标历史数据
     *
     * @param serviceName 服务名称
     * @param metricName 指标名称
     * @param hours 小时数
     * @return 历史数据
     */
    List<Map<String, Object>> queryMetricsHistory(String serviceName, String metricName, Integer hours);
}
