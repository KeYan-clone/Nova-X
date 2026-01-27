package com.novax.bff.consumer.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 会员服务客户端
 */
@FeignClient(name = "member-service", path = "/members")
public interface MemberClient {

    @GetMapping("/user/{userId}")
    Result<?> getMemberInfo(@PathVariable Long userId);

    @GetMapping("/points/{userId}")
    Result<?> getPoints(@PathVariable Long userId);

    @GetMapping("/balance/{userId}")
    Result<?> getBalance(@PathVariable Long userId);

    @PostMapping("/{userId}/recharge")
    Result<?> recharge(@PathVariable Long userId, @RequestBody Object dto);
}
