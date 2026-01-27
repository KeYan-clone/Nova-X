package com.novax.bff.utility.controller;

import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 电力公司电网管理控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "电力公司电网管理")
@RestController
@RequestMapping("/utility/grid")
@RequiredArgsConstructor
public class UtilityGridController {

  @Operation(summary = "电网负荷监控")
  @GetMapping("/load")
  public Result<Map<String, Object>> getGridLoad() {
    Map<String, Object> load = new HashMap<>();
    load.put("currentLoad", 85.6); // MW
    load.put("maxCapacity", 120.0);
    load.put("loadRate", 71.3); // %
    load.put("peakTime", "18:00-21:00");

    return Result.success(load);
  }

  @Operation(summary = "区域用电统计")
  @GetMapping("/regions/{regionId}/consumption")
  public Result<Map<String, Object>> getRegionConsumption(@PathVariable Long regionId) {
    Map<String, Object> consumption = new HashMap<>();
    consumption.put("regionId", regionId);
    consumption.put("todayConsumption", 1250.5); // kWh
    consumption.put("monthConsumption", 35000.0);
    consumption.put("yearConsumption", 420000.0);

    return Result.success(consumption);
  }

  @Operation(summary = "电价策略")
  @GetMapping("/pricing")
  public Result<Map<String, Object>> getPricingStrategy() {
    Map<String, Object> pricing = new HashMap<>();
    pricing.put("peakPrice", 1.2); // 元/kWh
    pricing.put("flatPrice", 0.8);
    pricing.put("valleyPrice", 0.4);

    return Result.success(pricing);
  }
}
