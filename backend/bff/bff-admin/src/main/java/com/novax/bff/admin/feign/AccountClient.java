package com.novax.bff.admin.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 账户服务客户端
 */
@FeignClient(name = "account-service", path = "/accounts")
public interface AccountClient {

  @GetMapping
  Result<?> getAccounts(@RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size);

  @GetMapping("/{accountId}")
  Result<?> getAccount(@PathVariable Long accountId);

  @PostMapping("/{accountId}/disable")
  Result<?> disableAccount(@PathVariable Long accountId);

  @PostMapping("/{accountId}/enable")
  Result<?> enableAccount(@PathVariable Long accountId);
}
