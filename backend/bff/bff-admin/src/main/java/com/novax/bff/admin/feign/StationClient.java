package com.novax.bff.admin.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "station-service", path = "/stations")
public interface StationClient {

    @GetMapping("/{id}")
    Result<?> getById(@PathVariable Long id);

    @PostMapping
    Result<?> create(@RequestBody Object dto);

    @GetMapping("/list")
    Result<?> list(@RequestParam(required = false) String keyword);
}
