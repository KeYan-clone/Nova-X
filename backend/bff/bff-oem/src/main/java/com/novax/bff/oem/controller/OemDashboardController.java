package com.novax.bff.oem.controller;

import com.novax.bff.oem.feign.DeviceClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * OEM仪表盘控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "OEM仪表盘")
@RestController
@RequestMapping("/oem/dashboard")
@RequiredArgsConstructor
public class OemDashboardController {

  private final DeviceClient deviceClient;

  @Operation(summary = "OEM概览（聚合）")
  @GetMapping("/overview/{manufacturerId}")
  public Result<Map<String, Object>> getOverview(@PathVariable Long manufacturerId) {
    Map<String, Object> overview = new HashMap<>();

    // 聚合厂商设备数据
    overview.put("devices", deviceClient.getDevicesByManufacturer(manufacturerId).getData());
    overview.put("totalDevices", 1250);
    overview.put("onlineDevices", 1180);
    overview.put("offlineDevices", 70);
    overview.put("faultDevices", 15);
    overview.put("todayActivations", 35);

    return Result.success(overview);
  }

  @Operation(summary = "设备统计")
  @GetMapping("/stats/{manufacturerId}")
  public Result<Map<String, Object>> getStats(@PathVariable Long manufacturerId) {
    Map<String, Object> stats = new HashMap<>();

    stats.put("totalShipped", 5000);
    stats.put("totalActivated", 4500);
    stats.put("activationRate", "90%");
    stats.put("avgUptime", "99.2%");

    return Result.success(stats);
  }
}
