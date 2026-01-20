package com.novax.bff.operator.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "device-service", path = "/devices")
public interface DeviceClient {

    @GetMapping("/status-stats")
    Result<?> getStatusStats();

    @PostMapping("/{deviceId}/maintain")
    Result<?> setMaintenance(@PathVariable Long deviceId);
}
