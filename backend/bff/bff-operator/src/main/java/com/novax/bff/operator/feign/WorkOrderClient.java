package com.novax.bff.operator.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "work-order-service", path = "/work-orders")
public interface WorkOrderClient {

    @GetMapping("/pending")
    Result<?> getPendingOrders();

    @GetMapping("/my/{handlerId}")
    Result<?> getMyOrders(@PathVariable Long handlerId);

    @PostMapping("/{orderId}/assign")
    Result<?> assignOrder(@PathVariable Long orderId, @RequestParam Long handlerId);
}
