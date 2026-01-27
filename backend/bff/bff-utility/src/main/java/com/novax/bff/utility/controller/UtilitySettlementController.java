package com.novax.bff.utility.controller;

import com.novax.bff.utility.feign.SettlementClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 电力公司结算控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "电力公司结算")
@RestController
@RequestMapping("/utility/settlement")
@RequiredArgsConstructor
public class UtilitySettlementController {

  private final SettlementClient settlementClient;

  @Operation(summary = "创建结算单")
  @PostMapping
  public Result<?> createSettlement(@RequestBody Object dto) {
    return settlementClient.createSettlement(dto);
  }

  @Operation(summary = "待结算列表")
  @GetMapping("/pending")
  public Result<?> getPendingSettlements() {
    return settlementClient.getPendingSettlements();
  }

  @Operation(summary = "结算单详情")
  @GetMapping("/{settlementId}")
  public Result<?> getSettlement(@PathVariable Long settlementId) {
    return settlementClient.getSettlement(settlementId);
  }

  @Operation(summary = "确认结算")
  @PostMapping("/{settlementId}/confirm")
  public Result<?> confirmSettlement(@PathVariable Long settlementId) {
    return settlementClient.confirmSettlement(settlementId);
  }
}
