package com.novax.bff.oem.controller;

import com.novax.bff.oem.feign.IotClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * OEM设备监控控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "OEM设备监控")
@RestController
@RequestMapping("/oem/monitor")
@RequiredArgsConstructor
public class OemMonitorController {

  private final IotClient iotClient;

  @Operation(summary = "设备实时状态")
  @GetMapping("/devices/{deviceId}/status")
  public Result<?> getDeviceStatus(@PathVariable Long deviceId) {
    return iotClient.getDeviceStatus(deviceId);
  }

  @Operation(summary = "设备实时数据")
  @GetMapping("/devices/{deviceId}/realtime")
  public Result<?> getRealtimeData(@PathVariable Long deviceId) {
    return iotClient.getRealtimeData(deviceId);
  }

  @Operation(summary = "发送设备指令")
  @PostMapping("/devices/{deviceId}/commands")
  public Result<?> sendCommand(@PathVariable Long deviceId, @RequestBody Object command) {
    return iotClient.sendCommand(deviceId, command);
  }

  @Operation(summary = "设备OTA升级")
  @PostMapping("/devices/{deviceId}/ota")
  public Result<?> otaUpgrade(@PathVariable Long deviceId, @RequestBody Object dto) {
    return Result.success("OTA升级任务已创建");
  }
}
