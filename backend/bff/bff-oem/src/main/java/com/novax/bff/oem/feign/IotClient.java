package com.novax.bff.oem.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "iot-gateway-service", path = "/iot")
public interface IotClient {

    @GetMapping("/online/{deviceCode}")
    Result<?> checkDeviceOnline(@PathVariable String deviceCode);

    @PostMapping("/command/{deviceCode}")
    Result<?> sendCommand(@PathVariable String deviceCode, @RequestParam String command);
}
