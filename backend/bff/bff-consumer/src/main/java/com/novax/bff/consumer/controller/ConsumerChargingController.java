package com.novax.bff.consumer.controller;

import com.novax.bff.consumer.feign.MemberClient;
import com.novax.bff.consumer.feign.SessionClient;
import com.novax.bff.consumer.feign.StationClient;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户充电")
@RestController
@RequestMapping("/consumer")
@RequiredArgsConstructor
public class ConsumerChargingController {

    private final StationClient stationClient;
    private final SessionClient sessionClient;
    private final MemberClient memberClient;

    @Operation(summary = "查找附近充电站")
    @GetMapping("/nearby-stations")
    public Result<?> getNearbyStations(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "5") Integer radius) {
        return stationClient.getNearbyStations(latitude, longitude, radius);
    }

    @Operation(summary = "获取用户充电首页")
    @GetMapping("/home/{userId}")
    public Result<Map<String, Object>> getHome(@PathVariable Long userId) {
        Map<String, Object> home = new HashMap<>();

        home.put("memberInfo", memberClient.getMemberInfo(userId).getData());
        home.put("recentSessions", sessionClient.getUserSessions(userId).getData());

        return Result.success(home);
    }

    @Operation(summary = "启动充电")
    @PostMapping("/charging/start")
    public Result<?> startCharging(@RequestBody Object dto) {
        return sessionClient.startCharging(dto);
    }

    @Operation(summary = "停止充电")
    @PostMapping("/charging/{sessionId}/stop")
    public Result<?> stopCharging(@PathVariable Long sessionId) {
        return sessionClient.stopCharging(sessionId);
    }
}
