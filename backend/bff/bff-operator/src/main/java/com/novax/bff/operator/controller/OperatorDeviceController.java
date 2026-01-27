package com.novax.bff.operator.controller;

import com.novax.bff.operator.feign.DeviceClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 运营商设备管理控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "运营商设备管理")
@RestController
@RequestMapping("/operator/devices")
@RequiredArgsConstructor
public class OperatorDeviceController {

  private final DeviceClient deviceClient;

  @Operation(summary = "设备状态统计")
  @GetMapping("/status-stats")
  public Result<?> getStatusStats() {
    return deviceClient.getStatusStats();
  }

  @Operation(summary = "设置设备维护")
  @PostMapping("/{deviceId}/maintain")
  public Result<?> setMaintenance(@PathVariable Long deviceId) {
    return deviceClient.setMaintenance(deviceId);
  }
}
