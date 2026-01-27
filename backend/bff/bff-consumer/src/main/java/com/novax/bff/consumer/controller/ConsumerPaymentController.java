package com.novax.bff.consumer.controller;

import com.novax.bff.consumer.feign.BillingClient;
import com.novax.bff.consumer.feign.PaymentClient;
import com.novax.bff.consumer.feign.SessionClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户支付控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "用户支付")
@RestController
@RequestMapping("/consumer/payment")
@RequiredArgsConstructor
public class ConsumerPaymentController {

  private final PaymentClient paymentClient;
  private final BillingClient billingClient;
  private final SessionClient sessionClient;

  @Operation(summary = "获取账单")
  @GetMapping("/bills/{sessionId}")
  public Result<?> getBill(@PathVariable Long sessionId) {
    return billingClient.getBill(sessionId);
  }

  @Operation(summary = "创建支付订单")
  @PostMapping("/create")
  public Result<?> createPayment(@RequestBody Object dto) {
    return paymentClient.createPayment(dto);
  }

  @Operation(summary = "支付记录")
  @GetMapping("/history/{userId}")
  public Result<?> getPaymentHistory(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "20") Integer size) {
    return paymentClient.getPaymentHistory(userId, page, size);
  }

  @Operation(summary = "获取充电账单详情（聚合）")
  @GetMapping("/charge-detail/{sessionId}")
  public Result<Map<String, Object>> getChargeDetail(@PathVariable Long sessionId) {
    Map<String, Object> detail = new HashMap<>();

    detail.put("session", sessionClient.getSession(sessionId).getData());
    detail.put("bill", billingClient.getBill(sessionId).getData());

    return Result.success(detail);
  }
}
