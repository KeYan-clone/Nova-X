package com.novax.bff.admin.controller;

import com.novax.bff.admin.feign.StationClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台充电站管理控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "管理后台充电站管理")
@RestController
@RequestMapping("/admin/stations")
@RequiredArgsConstructor
public class AdminStationController {

  private final StationClient stationClient;

  @Operation(summary = "充电站列表")
  @GetMapping
  public Result<?> getStations(@RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {
    return stationClient.getStations(keyword, page, size);
  }

  @Operation(summary = "充电站详情")
  @GetMapping("/{stationId}")
  public Result<?> getStation(@PathVariable Long stationId) {
    return stationClient.getStation(stationId);
  }

  @Operation(summary = "审核充电站")
  @PostMapping("/{stationId}/approve")
  public Result<?> approveStation(@PathVariable Long stationId) {
    // 可在此添加审核逻辑
    return Result.success();
  }

  @Operation(summary = "下线充电站")
  @PostMapping("/{stationId}/offline")
  public Result<?> offlineStation(@PathVariable Long stationId) {
    return stationClient.updateStationStatus(stationId, "OFFLINE");
  }
}
