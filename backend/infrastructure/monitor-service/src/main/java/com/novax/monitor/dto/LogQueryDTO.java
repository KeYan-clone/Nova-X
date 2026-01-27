package com.novax.monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志查询DTO
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Data
@Schema(description = "日志查询条件")
public class LogQueryDTO {

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "日志级别：INFO, WARN, ERROR")
    private String level;

    @Schema(description = "TraceId")
    private String traceId;

    @Schema(description = "关键词搜索")
    private String keyword;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页大小", example = "20")
    private Integer size = 20;
}
