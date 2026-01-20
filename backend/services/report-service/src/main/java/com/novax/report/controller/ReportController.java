package com.novax.report.controller;

import com.novax.common.core.result.Result;
import com.novax.report.service.ReportService;
import com.novax.report.vo.ChargingStatisticsVO;
import com.novax.report.vo.RevenueStatisticsVO;
import com.novax.report.vo.StationStatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "报表管理")
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "充电统计")
    @GetMapping("/charging")
    public Result<ChargingStatisticsVO> getChargingStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.success(reportService.getChargingStatistics(startDate, endDate));
    }

    @Operation(summary = "收入统计")
    @GetMapping("/revenue")
    public Result<RevenueStatisticsVO> getRevenueStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.success(reportService.getRevenueStatistics(startDate, endDate));
    }

    @Operation(summary = "充电站统计")
    @GetMapping("/stations")
    public Result<List<StationStatisticsVO>> getStationStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.success(reportService.getStationStatistics(startDate, endDate));
    }
}
