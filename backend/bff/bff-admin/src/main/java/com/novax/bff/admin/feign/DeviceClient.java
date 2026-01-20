package com.novax.bff.admin.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "device-service", path = "/devices")
public interface DeviceClient {

    @GetMapping("/{id}")
    Result<?> getById(@PathVariable Long id);

    @GetMapping("/station/{stationId}")
    Result<?> getByStationId(@PathVariable Long stationId);
}
