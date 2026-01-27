package com.novax.bff.consumer.controller;

import com.novax.bff.consumer.feign.MemberClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户会员控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "用户会员")
@RestController
@RequestMapping("/consumer/member")
@RequiredArgsConstructor
public class ConsumerMemberController {

  private final MemberClient memberClient;

  @Operation(summary = "获取会员信息")
  @GetMapping("/{userId}")
  public Result<?> getMemberInfo(@PathVariable Long userId) {
    return memberClient.getMemberInfo(userId);
  }

  @Operation(summary = "账户余额")
  @GetMapping("/{userId}/balance")
  public Result<?> getBalance(@PathVariable Long userId) {
    return memberClient.getBalance(userId);
  }

  @Operation(summary = "积分余额")
  @GetMapping("/{userId}/points")
  public Result<?> getPoints(@PathVariable Long userId) {
    return memberClient.getPoints(userId);
  }

  @Operation(summary = "余额充值")
  @PostMapping("/recharge")
  public Result<?> recharge(@RequestParam Long userId, @RequestBody Object dto) {
    return memberClient.recharge(userId, dto);
  }
}
