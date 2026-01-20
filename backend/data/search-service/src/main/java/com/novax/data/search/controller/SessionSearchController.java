package com.novax.data.search.controller;

import com.novax.common.core.result.Result;
import com.novax.data.search.service.SessionSearchService;
import com.novax.data.search.vo.SessionSearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 充电会话搜索控制器
 */
@Tag(name = "充电会话搜索")
@RestController
@RequestMapping("/search/sessions")
@RequiredArgsConstructor
public class SessionSearchController {

    private final SessionSearchService sessionSearchService;

    @Operation(summary = "根据会话ID搜索")
    @GetMapping("/{sessionId}")
    public Result<SessionSearchVO> searchBySessionId(@PathVariable String sessionId) {
        SessionSearchVO result = sessionSearchService.searchBySessionId(sessionId);
        return Result.success(result);
    }

    @Operation(summary = "根据用户ID搜索")
    @GetMapping("/user/{userId}")
    public Result<List<SessionSearchVO>> searchByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        List<SessionSearchVO> results = sessionSearchService.searchByUserId(userId, pageNum, pageSize);
        return Result.success(results);
    }

    @Operation(summary = "根据充电站ID搜索")
    @GetMapping("/station/{stationId}")
    public Result<List<SessionSearchVO>> searchByStationId(
            @PathVariable Long stationId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        List<SessionSearchVO> results = sessionSearchService.searchByStationId(
            stationId, status, pageNum, pageSize);
        return Result.success(results);
    }

    @Operation(summary = "按时间范围搜索")
    @GetMapping("/timerange")
    public Result<List<SessionSearchVO>> searchByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        List<SessionSearchVO> results = sessionSearchService.searchByTimeRange(
            startTime, endTime, pageNum, pageSize);
        return Result.success(results);
    }
}
