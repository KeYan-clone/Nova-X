package com.novax.bff.oem.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * IoT网关服务客户端
 */
@FeignClient(name = "iot-gateway-service", path = "/iot")
public interface IotClient {

    @GetMapping("/online/{deviceCode}")
    Result<?> checkDeviceOnline(@PathVariable String deviceCode);

    @GetMapping("/devices/{deviceId}/status")
    Result<?> getDeviceStatus(@PathVariable Long deviceId);

    @GetMapping("/devices/{deviceId}/realtime")
    Result<?> getRealtimeData(@PathVariable Long deviceId);

    @PostMapping("/command/{deviceCode}")
    Result<?> sendCommand(@PathVariable String deviceCode, @RequestParam String command);

    @PostMapping("/devices/{deviceId}/commands")
    Result<?> sendCommand(@PathVariable Long deviceId, @RequestBody Object command);
}
