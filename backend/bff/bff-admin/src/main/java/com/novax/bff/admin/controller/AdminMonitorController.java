package com.novax.bff.admin.controller;

import com.novax.bff.admin.feign.MonitorClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台系统监控控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "管理后台系统监控")
@RestController
@RequestMapping("/admin/monitor")
@RequiredArgsConstructor
public class AdminMonitorController {

  private final MonitorClient monitorClient;

  @Operation(summary = "服务健康状态")
  @GetMapping("/health")
  public Result<?> getServicesHealth() {
    return monitorClient.getServicesHealth();
  }

  @Operation(summary = "系统指标")
  @GetMapping("/metrics")
  public Result<?> getSystemMetrics() {
    return monitorClient.getSystemMetrics();
  }

  @Operation(summary = "查询日志")
  @GetMapping("/logs")
  public Result<?> queryLogs(@RequestParam String serviceId,
      @RequestParam(required = false) String level,
      @RequestParam(required = false) Long startTime,
      @RequestParam(required = false) Long endTime) {
    return monitorClient.queryLogs(serviceId, level, startTime, endTime);
  }
}
