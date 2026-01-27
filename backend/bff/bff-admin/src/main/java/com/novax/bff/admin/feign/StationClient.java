package com.novax.bff.admin.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 充电站服务客户端
 */
@FeignClient(name = "station-service", path = "/stations")
public interface StationClient {

    @GetMapping("/{id}")
    Result<?> getById(@PathVariable Long id);

    @GetMapping("/{id}")
    Result<?> getStation(@PathVariable("id") Long stationId);

    @PostMapping
    Result<?> create(@RequestBody Object dto);

    @GetMapping("/list")
    Result<?> list(@RequestParam(required = false) String keyword);

    @GetMapping
    Result<?> getStations(@RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size);

    @PostMapping("/{stationId}/status")
    Result<?> updateStationStatus(@PathVariable Long stationId, @RequestParam String status);
}
