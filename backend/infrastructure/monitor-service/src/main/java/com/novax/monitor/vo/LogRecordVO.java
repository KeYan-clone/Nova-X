package com.novax.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 日志记录VO
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "日志记录")
public class LogRecordVO {

    @Schema(description = "时间戳")
    private LocalDateTime timestamp;

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "日志级别")
    private String level;

    @Schema(description = "TraceId")
    private String traceId;

    @Schema(description = "类名")
    private String className;

    @Schema(description = "日志消息")
    private String message;

    @Schema(description = "异常信息")
    private String exception;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "请求URI")
    private String requestUri;

    @Schema(description = "客户端IP")
    private String clientIp;
}
