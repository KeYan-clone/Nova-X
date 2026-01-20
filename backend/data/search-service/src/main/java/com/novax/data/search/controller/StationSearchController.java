package com.novax.data.search.controller;

import com.novax.common.core.result.Result;
import com.novax.data.search.service.StationSearchService;
import com.novax.data.search.vo.StationSearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充电站搜索控制器
 */
@Tag(name = "充电站搜索")
@RestController
@RequestMapping("/search/stations")
@RequiredArgsConstructor
public class StationSearchController {

    private final StationSearchService stationSearchService;

    @Operation(summary = "全文搜索充电站")
    @GetMapping("/keyword")
    public Result<List<StationSearchVO>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        List<StationSearchVO> results = stationSearchService.searchStations(keyword, pageNum, pageSize);
        return Result.success(results);
    }

    @Operation(summary = "附近充电站搜索")
    @GetMapping("/nearby")
    public Result<List<StationSearchVO>> searchNearby(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "5") int radius,
            @RequestParam(defaultValue = "20") int pageSize) {

        List<StationSearchVO> results = stationSearchService.searchNearby(
            latitude, longitude, radius, pageSize);
        return Result.success(results);
    }

    @Operation(summary = "高级筛选搜索")
    @GetMapping("/advanced")
    public Result<List<StationSearchVO>> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) Integer minAvailableDevices,
            @RequestParam(defaultValue = "20") int pageSize) {

        List<StationSearchVO> results = stationSearchService.advancedSearch(
            keyword, operatorName, minAvailableDevices, pageSize);
        return Result.success(results);
    }
}
