package com.novax.bff.admin.controller;

import com.novax.bff.admin.feign.AccountClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台用户管理控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "管理后台用户管理")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

  private final AccountClient accountClient;

  @Operation(summary = "用户列表")
  @GetMapping
  public Result<?> getUsers(@RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {
    return accountClient.getAccounts(keyword, page, size);
  }

  @Operation(summary = "用户详情")
  @GetMapping("/{accountId}")
  public Result<?> getUser(@PathVariable Long accountId) {
    return accountClient.getAccount(accountId);
  }

  @Operation(summary = "禁用用户")
  @PostMapping("/{accountId}/disable")
  public Result<?> disableUser(@PathVariable Long accountId) {
    return accountClient.disableAccount(accountId);
  }

  @Operation(summary = "启用用户")
  @PostMapping("/{accountId}/enable")
  public Result<?> enableUser(@PathVariable Long accountId) {
    return accountClient.enableAccount(accountId);
  }
}
