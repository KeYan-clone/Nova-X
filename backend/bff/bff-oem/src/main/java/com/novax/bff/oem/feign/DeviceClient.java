package com.novax.bff.oem.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "device-service", path = "/devices")
public interface DeviceClient {

    @PostMapping("/register")
    Result<?> registerDevice(@RequestBody Object dto);

    @GetMapping("/manufacturer/{manufacturerId}")
    Result<?> getDevicesByManufacturer(@PathVariable Long manufacturerId);
}
