package com.novax.bff.consumer.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 支付服务客户端
 */
@FeignClient(name = "payment-service", path = "/payments")
public interface PaymentClient {

  @PostMapping("/create")
  Result<?> createPayment(@RequestBody Object dto);

  @GetMapping("/{paymentId}")
  Result<?> getPayment(@PathVariable Long paymentId);

  @GetMapping("/user/{userId}/history")
  Result<?> getPaymentHistory(@PathVariable Long userId,
      @RequestParam Integer page,
      @RequestParam Integer size);
}
