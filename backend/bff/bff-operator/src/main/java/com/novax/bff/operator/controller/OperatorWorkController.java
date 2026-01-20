package com.novax.bff.operator.controller;

import com.novax.bff.operator.feign.DeviceClient;
import com.novax.bff.operator.feign.WorkOrderClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "运营工作台")
@RestController
@RequestMapping("/operator")
@RequiredArgsConstructor
public class OperatorWorkController {

    private final WorkOrderClient workOrderClient;
    private final DeviceClient deviceClient;

    @Operation(summary = "获取工作台数据")
    @GetMapping("/workbench/{operatorId}")
    public Result<Map<String, Object>> getWorkbench(@PathVariable Long operatorId) {
        Map<String, Object> workbench = new HashMap<>();

        workbench.put("myOrders", workOrderClient.getMyOrders(operatorId).getData());
        workbench.put("pendingOrders", workOrderClient.getPendingOrders().getData());
        workbench.put("deviceStats", deviceClient.getStatusStats().getData());

        return Result.success(workbench);
    }

    @Operation(summary = "接单")
    @PostMapping("/orders/{orderId}/accept")
    public Result<?> acceptOrder(@PathVariable Long orderId, @RequestParam Long operatorId) {
        return workOrderClient.assignOrder(orderId, operatorId);
    }
}
