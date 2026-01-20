package com.novax.settlement.controller;

import com.novax.common.core.result.Result;
import com.novax.settlement.dto.CreateSettlementDTO;
import com.novax.settlement.entity.SettlementOrder;
import com.novax.settlement.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "结算管理")
@RestController
@RequestMapping("/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @Operation(summary = "创建结算单")
    @PostMapping
    public Result<SettlementOrder> createSettlement(@Valid @RequestBody CreateSettlementDTO dto) {
        return Result.success(settlementService.createSettlement(dto));
    }

    @Operation(summary = "确认结算单")
    @PostMapping("/{settlementId}/confirm")
    public Result<Boolean> confirmSettlement(@PathVariable Long settlementId) {
        return Result.success(settlementService.confirmSettlement(settlementId));
    }

    @Operation(summary = "支付结算单")
    @PostMapping("/{settlementId}/pay")
    public Result<Boolean> paySettlement(@PathVariable Long settlementId) {
        return Result.success(settlementService.paySettlement(settlementId));
    }

    @Operation(summary = "查询结算单详情")
    @GetMapping("/{settlementId}")
    public Result<SettlementOrder> getById(@PathVariable Long settlementId) {
        return Result.success(settlementService.getById(settlementId));
    }

    @Operation(summary = "获取待结算列表")
    @GetMapping("/pending")
    public Result<List<SettlementOrder>> getPendingSettlements() {
        return Result.success(settlementService.getPendingSettlements());
    }
}
