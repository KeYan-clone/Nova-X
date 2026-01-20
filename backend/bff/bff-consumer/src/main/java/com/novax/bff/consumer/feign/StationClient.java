package com.novax.bff.consumer.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "station-service", path = "/stations")
public interface StationClient {

    @GetMapping("/nearby")
    Result<?> getNearbyStations(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "5") Integer radius);

    @GetMapping("/{id}")
    Result<?> getById(@PathVariable Long id);
}
