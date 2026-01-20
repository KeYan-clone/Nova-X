package com.novax.device.controller;

import com.novax.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "设备管理")
@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Operation(summary = "获取设备列表")
    @GetMapping
    public Result<?> list() {
        return Result.success();
    }

    @Operation(summary = "获取设备状态")
    @GetMapping("/{id}/status")
    public Result<?> getStatus(@PathVariable String id) {
        return Result.success();
    }
}
