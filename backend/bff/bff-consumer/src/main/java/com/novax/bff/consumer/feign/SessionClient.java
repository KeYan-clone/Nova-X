package com.novax.bff.consumer.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 充电会话服务客户端
 */
@FeignClient(name = "session-service", path = "/sessions")
public interface SessionClient {

    @PostMapping("/start")
    Result<?> startCharging(@RequestBody Object dto);

    @PostMapping("/{sessionId}/stop")
    Result<?> stopCharging(@PathVariable Long sessionId);

    @GetMapping("/{sessionId}")
    Result<?> getSession(@PathVariable Long sessionId);

    @GetMapping("/user/{userId}")
    Result<?> getUserSessions(@PathVariable Long userId);
}
