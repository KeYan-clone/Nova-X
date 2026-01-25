package com.novax.bff.consumer.controller;

import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 充电站控制器
 */
@Slf4j
@Tag(name = "充电站", description = "充电站相关接口")
@RestController
@RequestMapping("/stations")
public class StationController {

    @Operation(summary = "查找附近充电站")
    @GetMapping
    public Result<?> findNearby(@RequestParam Double lng,
            @RequestParam Double lat,
            @RequestParam(required = false) Integer radius) {
        log.info("查找附近充电站: lng={}, lat={}, radius={}", lng, lat, radius);
        return Result.success();
    }
}
