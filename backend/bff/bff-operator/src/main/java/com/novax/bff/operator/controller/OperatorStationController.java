package com.novax.bff.operator.controller;

import com.novax.bff.operator.feign.StationClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 运营商站点管理控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "运营商站点管理")
@RestController
@RequestMapping("/operator/stations")
@RequiredArgsConstructor
public class OperatorStationController {

  private final StationClient stationClient;

  @Operation(summary = "站点列表")
  @GetMapping
  public Result<?> listStations(
      @RequestParam(required = false) Long operatorId,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "20") Integer size) {
    return stationClient.listStations(operatorId, page, size);
  }

  @Operation(summary = "站点详情")
  @GetMapping("/{stationId}")
  public Result<?> getStation(@PathVariable Long stationId) {
    return stationClient.getStation(stationId);
  }

  @Operation(summary = "创建站点")
  @PostMapping
  public Result<?> createStation(@RequestBody Object dto) {
    return stationClient.createStation(dto);
  }

  @Operation(summary = "更新站点")
  @PutMapping("/{stationId}")
  public Result<?> updateStation(@PathVariable Long stationId, @RequestBody Object dto) {
    return stationClient.updateStation(stationId, dto);
  }

  @Operation(summary = "站点设备列表")
  @GetMapping("/{stationId}/devices")
  public Result<?> getStationDevices(@PathVariable Long stationId) {
    return stationClient.getStationDevices(stationId);
  }
}
