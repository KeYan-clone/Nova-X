package com.novax.bff.operator.controller;

import com.novax.bff.operator.feign.ReportClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 运营商报表控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "运营商报表")
@RestController
@RequestMapping("/operator/reports")
@RequiredArgsConstructor
public class OperatorReportController {

  private final ReportClient reportClient;

  @Operation(summary = "充电统计")
  @GetMapping("/charging")
  public Result<?> getChargingStatistics(
      @RequestParam String startDate,
      @RequestParam String endDate) {
    return reportClient.getChargingStatistics(startDate, endDate);
  }

  @Operation(summary = "收入统计")
  @GetMapping("/revenue")
  public Result<?> getRevenueStatistics(
      @RequestParam String startDate,
      @RequestParam String endDate) {
    return reportClient.getRevenueStatistics(startDate, endDate);
  }

  @Operation(summary = "站点统计")
  @GetMapping("/stations")
  public Result<?> getStationStatistics(
      @RequestParam String startDate,
      @RequestParam String endDate) {
    return reportClient.getStationStatistics(startDate, endDate);
  }

  @Operation(summary = "运营概览（聚合）")
  @GetMapping("/overview")
  public Result<Map<String, Object>> getOverview(
      @RequestParam String startDate,
      @RequestParam String endDate) {
    Map<String, Object> overview = new HashMap<>();

    overview.put("charging", reportClient.getChargingStatistics(startDate, endDate).getData());
    overview.put("revenue", reportClient.getRevenueStatistics(startDate, endDate).getData());
    overview.put("stations", reportClient.getStationStatistics(startDate, endDate).getData());

    return Result.success(overview);
  }
}
