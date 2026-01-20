package com.novax.bff.utility.controller;

import com.novax.bff.utility.feign.DrVppClient;
import com.novax.bff.utility.feign.SettlementClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "电力公司需求响应")
@RestController
@RequestMapping("/utility")
@RequiredArgsConstructor
public class UtilityDemandResponseController {

    private final DrVppClient drVppClient;
    private final SettlementClient settlementClient;

    @Operation(summary = "创建需求响应事件")
    @PostMapping("/dr-events")
    public Result<?> createDrEvent(@RequestBody Object dto) {
        return drVppClient.createEvent(dto);
    }

    @Operation(summary = "获取需求响应概览")
    @GetMapping("/dr-overview")
    public Result<Map<String, Object>> getDrOverview() {
        Map<String, Object> overview = new HashMap<>();

        overview.put("activeEvents", drVppClient.getActiveEvents().getData());
        overview.put("availableDevices", drVppClient.getAvailableDevices().getData());

        return Result.success(overview);
    }

    @Operation(summary = "创建结算单")
    @PostMapping("/settlements")
    public Result<?> createSettlement(@RequestBody Object dto) {
        return settlementClient.createSettlement(dto);
    }

    @Operation(summary = "获取待结算列表")
    @GetMapping("/settlements/pending")
    public Result<?> getPendingSettlements() {
        return settlementClient.getPendingSettlements();
    }
}
