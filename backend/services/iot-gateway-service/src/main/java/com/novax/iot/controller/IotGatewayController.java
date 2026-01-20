package com.novax.iot.controller;

import com.novax.common.core.result.Result;
import com.novax.iot.dto.DeviceDataDTO;
import com.novax.iot.service.IotGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "物联网网关")
@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
public class IotGatewayController {

    private final IotGatewayService iotGatewayService;

    @Operation(summary = "接收设备数据")
    @PostMapping("/data")
    public Result<Boolean> receiveDeviceData(@RequestBody DeviceDataDTO dto) {
        return Result.success(iotGatewayService.receiveDeviceData(dto));
    }

    @Operation(summary = "发送设备指令")
    @PostMapping("/command/{deviceCode}")
    public Result<Boolean> sendCommand(
            @PathVariable String deviceCode,
            @RequestParam String command,
            @RequestBody(required = false) Object params) {
        return Result.success(iotGatewayService.sendCommand(deviceCode, command, params));
    }

    @Operation(summary = "检查设备在线状态")
    @GetMapping("/online/{deviceCode}")
    public Result<Boolean> checkDeviceOnline(@PathVariable String deviceCode) {
        return Result.success(iotGatewayService.checkDeviceOnline(deviceCode));
    }
}
