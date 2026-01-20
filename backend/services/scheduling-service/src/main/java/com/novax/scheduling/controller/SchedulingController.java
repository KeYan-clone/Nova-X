package com.novax.scheduling.controller;

import com.novax.common.core.result.Result;
import com.novax.scheduling.dto.CreateReservationDTO;
import com.novax.scheduling.entity.ChargingReservation;
import com.novax.scheduling.service.SchedulingService;
import com.novax.scheduling.vo.AvailableDeviceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "调度管理")
@RestController
@RequestMapping("/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService schedulingService;

    @Operation(summary = "查询可用设备")
    @GetMapping("/available-devices")
    public Result<List<AvailableDeviceVO>> getAvailableDevices(
            @RequestParam Long stationId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(schedulingService.getAvailableDevices(stationId, startTime, endTime));
    }

    @Operation(summary = "创建预约")
    @PostMapping("/reservations")
    public Result<ChargingReservation> createReservation(@Valid @RequestBody CreateReservationDTO dto) {
        return Result.success(schedulingService.createReservation(dto));
    }

    @Operation(summary = "取消预约")
    @PostMapping("/reservations/{reservationId}/cancel")
    public Result<Boolean> cancelReservation(@PathVariable Long reservationId, @RequestParam String reason) {
        return Result.success(schedulingService.cancelReservation(reservationId, reason));
    }

    @Operation(summary = "获取用户预约")
    @GetMapping("/reservations/user/{userId}")
    public Result<List<ChargingReservation>> getUserReservations(@PathVariable Long userId) {
        return Result.success(schedulingService.getUserReservations(userId));
    }

    @Operation(summary = "查询预约详情")
    @GetMapping("/reservations/{reservationId}")
    public Result<ChargingReservation> getById(@PathVariable Long reservationId) {
        return Result.success(schedulingService.getById(reservationId));
    }
}
