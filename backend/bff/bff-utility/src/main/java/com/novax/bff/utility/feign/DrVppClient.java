package com.novax.bff.utility.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "dr-vpp-service", path = "/dr-vpp")
public interface DrVppClient {

    @PostMapping("/events")
    Result<?> createEvent(@RequestBody Object dto);

    @GetMapping("/events/active")
    Result<?> getActiveEvents();

    @GetMapping("/devices/available")
    Result<?> getAvailableDevices();
}
