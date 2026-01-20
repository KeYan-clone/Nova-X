package com.novax.workorder.controller;

import com.novax.common.core.result.Result;
import com.novax.workorder.dto.CreateWorkOrderDTO;
import com.novax.workorder.entity.WorkOrder;
import com.novax.workorder.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "工单管理")
@RestController
@RequestMapping("/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @Operation(summary = "创建工单")
    @PostMapping
    public Result<WorkOrder> createWorkOrder(@Valid @RequestBody CreateWorkOrderDTO dto) {
        return Result.success(workOrderService.createWorkOrder(dto));
    }

    @Operation(summary = "分配工单")
    @PostMapping("/{orderId}/assign")
    public Result<Boolean> assignWorkOrder(@PathVariable Long orderId, @RequestParam Long handlerId) {
        return Result.success(workOrderService.assignWorkOrder(orderId, handlerId));
    }

    @Operation(summary = "开始处理")
    @PostMapping("/{orderId}/start")
    public Result<Boolean> startHandle(@PathVariable Long orderId) {
        return Result.success(workOrderService.startHandle(orderId));
    }

    @Operation(summary = "完成工单")
    @PostMapping("/{orderId}/complete")
    public Result<Boolean> completeWorkOrder(@PathVariable Long orderId, @RequestParam String handleResult) {
        return Result.success(workOrderService.completeWorkOrder(orderId, handleResult));
    }

    @Operation(summary = "查询工单详情")
    @GetMapping("/{orderId}")
    public Result<WorkOrder> getById(@PathVariable Long orderId) {
        return Result.success(workOrderService.getById(orderId));
    }

    @Operation(summary = "获取待处理工单")
    @GetMapping("/pending")
    public Result<List<WorkOrder>> getPendingOrders() {
        return Result.success(workOrderService.getPendingOrders());
    }

    @Operation(summary = "获取我的工单")
    @GetMapping("/my/{handlerId}")
    public Result<List<WorkOrder>> getMyOrders(@PathVariable Long handlerId) {
        return Result.success(workOrderService.getMyOrders(handlerId));
    }
}
