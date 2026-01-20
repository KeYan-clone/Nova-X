package com.novax.data.sync.controller;

import com.novax.common.core.result.Result;
import com.novax.data.sync.service.SessionSyncService;
import com.novax.data.sync.service.StationSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 数据同步控制器
 */
@Tag(name = "数据同步")
@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
public class SyncController {

    private final StationSyncService stationSyncService;
    private final SessionSyncService sessionSyncService;

    @Operation(summary = "手动触发充电站全量同步")
    @PostMapping("/stations/full")
    public Result<Void> syncAllStations() {
        stationSyncService.syncAllStations();
        return Result.success();
    }

    @Operation(summary = "手动触发充电站增量同步")
    @PostMapping("/stations/{stationId}")
    public Result<Void> syncStation(@PathVariable Long stationId) {
        stationSyncService.syncStation(stationId);
        return Result.success();
    }

    @Operation(summary = "手动触发充电会话增量同步")
    @PostMapping("/sessions/recent")
    public Result<Void> syncRecentSessions() {
        sessionSyncService.syncRecentSessions();
        return Result.success();
    }

    @Operation(summary = "手动触发单个会话同步")
    @PostMapping("/sessions/{sessionId}")
    public Result<Void> syncSession(@PathVariable Long sessionId) {
        sessionSyncService.syncSession(sessionId);
        return Result.success();
    }
}
