package com.novax.bff.oem.controller;

import com.novax.bff.oem.feign.DeviceClient;
import com.novax.bff.oem.feign.IotClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "厂商设备管理")
@RestController
@RequestMapping("/oem")
@RequiredArgsConstructor
public class OemDeviceController {

    private final DeviceClient deviceClient;
    private final IotClient iotClient;

    @Operation(summary = "注册设备")
    @PostMapping("/devices/register")
    public Result<?> registerDevice(@RequestBody Object dto) {
        return deviceClient.registerDevice(dto);
    }

    @Operation(summary = "获取厂商设备列表")
    @GetMapping("/devices/manufacturer/{manufacturerId}")
    public Result<?> getDevicesList(@PathVariable Long manufacturerId) {
        return deviceClient.getDevicesByManufacturer(manufacturerId);
    }

    @Operation(summary = "远程控制设备")
    @PostMapping("/devices/{deviceCode}/control")
    public Result<?> controlDevice(
            @PathVariable String deviceCode,
            @RequestParam String command) {
        return iotClient.sendCommand(deviceCode, command);
    }
}
