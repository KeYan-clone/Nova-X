package com.novax.drvpp.controller;

import com.novax.common.core.result.Result;
import com.novax.drvpp.dto.CreateEventDTO;
import com.novax.drvpp.entity.DemandResponseEvent;
import com.novax.drvpp.entity.VppDevice;
import com.novax.drvpp.service.DrVppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "需求响应管理")
@RestController
@RequestMapping("/dr-vpp")
@RequiredArgsConstructor
public class DrVppController {

    private final DrVppService drVppService;

    @Operation(summary = "创建需求响应事件")
    @PostMapping("/events")
    public Result<DemandResponseEvent> createEvent(@Valid @RequestBody CreateEventDTO dto) {
        return Result.success(drVppService.createEvent(dto));
    }

    @Operation(summary = "启动事件")
    @PostMapping("/events/{eventId}/start")
    public Result<Boolean> startEvent(@PathVariable Long eventId) {
        return Result.success(drVppService.startEvent(eventId));
    }

    @Operation(summary = "完成事件")
    @PostMapping("/events/{eventId}/complete")
    public Result<Boolean> completeEvent(@PathVariable Long eventId) {
        return Result.success(drVppService.completeEvent(eventId));
    }

    @Operation(summary = "查询可用设备")
    @GetMapping("/devices/available")
    public Result<List<VppDevice>> getAvailableDevices() {
        return Result.success(drVppService.getAvailableDevices());
    }

    @Operation(summary = "查询进行中事件")
    @GetMapping("/events/active")
    public Result<List<DemandResponseEvent>> getActiveEvents() {
        return Result.success(drVppService.getActiveEvents());
    }

    @Operation(summary = "查询事件详情")
    @GetMapping("/events/{eventId}")
    public Result<DemandResponseEvent> getEventById(@PathVariable Long eventId) {
        return Result.success(drVppService.getEventById(eventId));
    }
}
