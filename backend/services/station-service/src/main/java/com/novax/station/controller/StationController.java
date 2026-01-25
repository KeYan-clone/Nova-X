package com.novax.station.controller;

import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "站点管理")
@RestController
@RequestMapping("/stations")
public class StationController {

    @Operation(summary = "创建站点")
    @PostMapping
    public Result<?> create(@RequestBody Object request) {
        return Result.success();
    }

    @Operation(summary = "获取站点详情")
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable String id) {
        return Result.success();
    }
}
