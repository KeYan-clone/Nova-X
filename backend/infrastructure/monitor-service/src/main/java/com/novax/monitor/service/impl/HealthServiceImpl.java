package com.novax.monitor.service.impl;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.novax.monitor.service.HealthService;
import com.novax.monitor.vo.ServiceHealthVO;
import com.novax.monitor.vo.SystemOverviewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 健康检查服务实现
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthServiceImpl implements HealthService {

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public SystemOverviewVO getSystemOverview() {
        log.info("获取系统概览");

        List<ServiceHealthVO> healthList = getAllServicesHealth();

        long healthyCount = healthList.stream()
                .filter(h -> "UP".equals(h.getStatus()))
                .count();

        long unhealthyCount = healthList.stream()
                .filter(h -> "DOWN".equals(h.getStatus()) || "UNKNOWN".equals(h.getStatus()))
                .count();

        return SystemOverviewVO.builder()
                .totalServices(healthList.size())
                .healthyServices((int) healthyCount)
                .unhealthyServices((int) unhealthyCount)
                .totalQps(15000L)  // TODO: 从 Prometheus 获取实际数据
                .avgResponseTime(85L)
                .errorRate(0.12)
                .todayTotalRequests(1_250_000L)
                .todayErrors(1500L)
                .serviceHealthList(healthList)
                .build();
    }

    @Override
    public List<ServiceHealthVO> getAllServicesHealth() {
        log.info("获取所有服务健康状态");

        List<ServiceHealthVO> healthList = new ArrayList<>();
        List<String> services = discoveryClient.getServices();

        for (String serviceName : services) {
            try {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                for (ServiceInstance instance : instances) {
                    ServiceHealthVO health = checkHealth(serviceName, instance.getInstanceId());
                    healthList.add(health);
                }
            } catch (Exception e) {
                log.error("获取服务 {} 健康状态失败", serviceName, e);
                healthList.add(ServiceHealthVO.builder()
                        .serviceName(serviceName)
                        .status("UNKNOWN")
                        .error(e.getMessage())
                        .lastCheckTime(LocalDateTime.now().format(FORMATTER))
                        .build());
            }
        }

        return healthList;
    }

    @Override
    public ServiceHealthVO getServiceHealth(String serviceName) {
        log.info("获取服务健康状态: {}", serviceName);

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            return ServiceHealthVO.builder()
                    .serviceName(serviceName)
                    .status("DOWN")
                    .error("服务未注册或无可用实例")
                    .lastCheckTime(LocalDateTime.now().format(FORMATTER))
                    .build();
        }

        ServiceInstance instance = instances.get(0);
        return checkHealth(serviceName, instance.getInstanceId());
    }

    @Override
    public ServiceHealthVO checkHealth(String serviceName, String instanceId) {
        log.debug("检查服务健康: serviceName={}, instanceId={}", serviceName, instanceId);

        try {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            if (instances.isEmpty()) {
                return buildHealthVO(serviceName, instanceId, null, "DOWN", "无可用实例", null);
            }

            ServiceInstance instance = instances.get(0);
            String healthUrl = String.format("http://%s:%d/actuator/health",
                    instance.getHost(), instance.getPort());

            long startTime = System.currentTimeMillis();
            try {
                String response = restTemplate.getForObject(healthUrl, String.class);
                long responseTime = System.currentTimeMillis() - startTime;

                boolean isUp = response != null && response.contains("\"status\":\"UP\"");
                return buildHealthVO(serviceName, instanceId,
                        instance.getHost() + ":" + instance.getPort(),
                        isUp ? "UP" : "DOWN",
                        null, responseTime);

            } catch (Exception e) {
                long responseTime = System.currentTimeMillis() - startTime;
                return buildHealthVO(serviceName, instanceId,
                        instance.getHost() + ":" + instance.getPort(),
                        "DOWN",
                        "健康检查失败: " + e.getMessage(),
                        responseTime);
            }

        } catch (Exception e) {
            log.error("检查服务健康失败: serviceName={}, instanceId={}", serviceName, instanceId, e);
            return buildHealthVO(serviceName, instanceId, null, "UNKNOWN", e.getMessage(), null);
        }
    }

    private ServiceHealthVO buildHealthVO(String serviceName, String instanceId,
                                          String address, String status,
                                          String error, Long responseTime) {
        return ServiceHealthVO.builder()
                .serviceName(serviceName)
                .instanceId(instanceId)
                .address(address)
                .status(status)
                .responseTime(responseTime)
                .error(error)
                .lastCheckTime(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}
