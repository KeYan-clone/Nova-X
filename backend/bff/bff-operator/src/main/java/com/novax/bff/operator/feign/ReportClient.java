package com.novax.bff.operator.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 报表服务客户端
 */
@FeignClient(name = "report-service", path = "/reports")
public interface ReportClient {

  @GetMapping("/charging")
  Result<?> getChargingStatistics(@RequestParam String startDate,
      @RequestParam String endDate);

  @GetMapping("/revenue")
  Result<?> getRevenueStatistics(@RequestParam String startDate,
      @RequestParam String endDate);

  @GetMapping("/stations")
  Result<?> getStationStatistics(@RequestParam String startDate,
      @RequestParam String endDate);
}
