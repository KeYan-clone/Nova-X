package com.novax.bff.consumer.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 计费服务客户端
 */
@FeignClient(name = "billing-service", path = "/billing")
public interface BillingClient {

  @GetMapping("/bills/{sessionId}")
  Result<?> getBill(@PathVariable Long sessionId);

  @GetMapping("/bills/user/{userId}/recent")
  Result<?> getRecentBills(@PathVariable Long userId,
      @RequestParam(defaultValue = "10") Integer limit);
}
