package com.novax.bff.consumer.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "session-service", path = "/sessions")
public interface SessionClient {

    @PostMapping("/start")
    Result<?> startCharging(@RequestBody Object dto);

    @PostMapping("/{sessionId}/stop")
    Result<?> stopCharging(@PathVariable Long sessionId);

    @GetMapping("/user/{userId}")
    Result<?> getUserSessions(@PathVariable Long userId);
}
