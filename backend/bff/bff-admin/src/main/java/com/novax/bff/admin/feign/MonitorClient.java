package com.novax.bff.admin.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 监控服务客户端
 */
@FeignClient(name = "monitor-service", path = "/monitor")
public interface MonitorClient {

  @GetMapping("/health")
  Result<?> getServicesHealth();

  @GetMapping("/metrics")
  Result<?> getSystemMetrics();

  @GetMapping("/logs")
  Result<?> queryLogs(@RequestParam String serviceId,
      @RequestParam(required = false) String level,
      @RequestParam(required = false) Long startTime,
      @RequestParam(required = false) Long endTime);
}
