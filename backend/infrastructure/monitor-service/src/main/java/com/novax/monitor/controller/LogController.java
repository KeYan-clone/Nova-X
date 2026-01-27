package com.novax.monitor.controller;

import com.novax.common.core.result.PageResult;
import com.novax.common.core.result.Result;
import com.novax.monitor.dto.LogQueryDTO;
import com.novax.monitor.service.LogService;
import com.novax.monitor.vo.LogRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 日志管理控制器
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Tag(name = "日志管理", description = "日志查询与分析")
@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "查询日志")
    @PostMapping("/query")
    public Result<PageResult<LogRecordVO>> queryLogs(@Valid @RequestBody LogQueryDTO queryDTO) {
        PageResult<LogRecordVO> result = logService.queryLogs(queryDTO);
        return Result.success(result);
    }

    @Operation(summary = "根据TraceId查询链路日志")
    @GetMapping("/trace/{traceId}")
    public Result<List<LogRecordVO>> queryByTraceId(
            @Parameter(description = "TraceId") @PathVariable String traceId) {
        List<LogRecordVO> logs = logService.queryByTraceId(traceId);
        return Result.success(logs);
    }

    @Operation(summary = "统计错误日志数量")
    @GetMapping("/errors/count")
    public Result<Long> countErrors(
            @Parameter(description = "服务名称") @RequestParam(required = false) String serviceName,
            @Parameter(description = "小时数") @RequestParam(defaultValue = "24") Integer hours) {
        Long count = logService.countErrors(serviceName, hours);
        return Result.success(count);
    }
}
