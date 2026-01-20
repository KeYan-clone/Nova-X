package com.novax.bff.admin.controller;

import com.novax.bff.admin.feign.DeviceClient;
import com.novax.bff.admin.feign.ReportClient;
import com.novax.bff.admin.feign.StationClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "管理后台")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final StationClient stationClient;
    private final DeviceClient deviceClient;
    private final ReportClient reportClient;

    @Operation(summary = "获取仪表盘数据")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboard(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("chargingStats", reportClient.getChargingStatistics(startDate, endDate).getData());
        dashboard.put("revenueStats", reportClient.getRevenueStatistics(startDate, endDate).getData());
        dashboard.put("stationStats", reportClient.getStationStatistics(startDate, endDate).getData());

        return Result.success(dashboard);
    }

    @Operation(summary = "获取充电站详情及设备")
    @GetMapping("/stations/{stationId}/detail")
    public Result<Map<String, Object>> getStationDetail(@PathVariable Long stationId) {
        Map<String, Object> detail = new HashMap<>();

        detail.put("station", stationClient.getById(stationId).getData());
        detail.put("devices", deviceClient.getByStationId(stationId).getData());

        return Result.success(detail);
    }
}
