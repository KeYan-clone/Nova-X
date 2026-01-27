package com.novax.monitor.service;

import com.novax.monitor.vo.ServiceHealthVO;
import com.novax.monitor.vo.SystemOverviewVO;

import java.util.List;

/**
 * 健康检查服务接口
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
public interface HealthService {

    /**
     * 获取系统概览
     *
     * @return 系统概览
     */
    SystemOverviewVO getSystemOverview();

    /**
     * 获取所有服务健康状态
     *
     * @return 服务健康状态列表
     */
    List<ServiceHealthVO> getAllServicesHealth();

    /**
     * 获取指定服务健康状态
     *
     * @param serviceName 服务名称
     * @return 服务健康状态
     */
    ServiceHealthVO getServiceHealth(String serviceName);

    /**
     * 检查服务健康状态
     *
     * @param serviceName 服务名称
     * @param instanceId 实例ID
     * @return 健康状态
     */
    ServiceHealthVO checkHealth(String serviceName, String instanceId);
}
