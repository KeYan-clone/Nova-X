package com.novax.monitor.controller;

import com.novax.common.core.result.Result;
import com.novax.monitor.service.HealthService;
import com.novax.monitor.vo.ServiceHealthVO;
import com.novax.monitor.vo.SystemOverviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 健康检查控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "健康检查", description = "服务健康状态监控")
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @Operation(summary = "获取系统概览")
    @GetMapping("/overview")
    public Result<SystemOverviewVO> getSystemOverview() {
        SystemOverviewVO overview = healthService.getSystemOverview();
        return Result.success(overview);
    }

    @Operation(summary = "获取所有服务健康状态")
    @GetMapping("/services")
    public Result<List<ServiceHealthVO>> getAllServicesHealth() {
        List<ServiceHealthVO> healthList = healthService.getAllServicesHealth();
        return Result.success(healthList);
    }

    @Operation(summary = "获取指定服务健康状态")
    @GetMapping("/services/{serviceName}")
    public Result<ServiceHealthVO> getServiceHealth(
            @Parameter(description = "服务名称") @PathVariable String serviceName) {
        ServiceHealthVO health = healthService.getServiceHealth(serviceName);
        return Result.success(health);
    }

    @Operation(summary = "检查服务健康")
    @GetMapping("/check")
    public Result<ServiceHealthVO> checkHealth(
            @Parameter(description = "服务名称") @RequestParam String serviceName,
            @Parameter(description = "实例ID") @RequestParam(required = false) String instanceId) {
        ServiceHealthVO health = healthService.checkHealth(serviceName, instanceId);
        return Result.success(health);
    }
}
