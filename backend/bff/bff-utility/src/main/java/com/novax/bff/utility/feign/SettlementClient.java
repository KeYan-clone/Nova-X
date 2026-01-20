package com.novax.bff.utility.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "settlement-service", path = "/settlements")
public interface SettlementClient {

    @PostMapping
    Result<?> createSettlement(@RequestBody Object dto);

    @GetMapping("/pending")
    Result<?> getPendingSettlements();

    @PostMapping("/{settlementId}/confirm")
    Result<?> confirmSettlement(@PathVariable Long settlementId);
}
