package com.novax.monitor.controller;

import com.novax.common.core.result.Result;
import com.novax.monitor.service.MetricsService;
import com.novax.monitor.vo.ServiceMetricsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 指标监控控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "指标监控", description = "服务性能指标监控")
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @Operation(summary = "获取服务指标")
    @GetMapping("/services/{serviceName}")
    public Result<ServiceMetricsVO> getServiceMetrics(
            @Parameter(description = "服务名称") @PathVariable String serviceName) {
        ServiceMetricsVO metrics = metricsService.getServiceMetrics(serviceName);
        return Result.success(metrics);
    }

    @Operation(summary = "获取所有服务指标")
    @GetMapping("/services")
    public Result<List<ServiceMetricsVO>> getAllServicesMetrics() {
        List<ServiceMetricsVO> metricsList = metricsService.getAllServicesMetrics();
        return Result.success(metricsList);
    }

    @Operation(summary = "获取自定义指标")
    @GetMapping("/custom")
    public Result<Map<String, Object>> getCustomMetrics(
            @Parameter(description = "服务名称") @RequestParam String serviceName,
            @Parameter(description = "指标名称") @RequestParam String metricName) {
        Map<String, Object> metrics = metricsService.getCustomMetrics(serviceName, metricName);
        return Result.success(metrics);
    }

    @Operation(summary = "查询指标历史")
    @GetMapping("/history")
    public Result<List<Map<String, Object>>> queryMetricsHistory(
            @Parameter(description = "服务名称") @RequestParam String serviceName,
            @Parameter(description = "指标名称") @RequestParam String metricName,
            @Parameter(description = "小时数") @RequestParam(defaultValue = "24") Integer hours) {
        List<Map<String, Object>> history = metricsService.queryMetricsHistory(serviceName, metricName, hours);
        return Result.success(history);
    }
}
