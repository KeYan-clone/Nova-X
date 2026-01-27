package com.novax.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 服务指标VO
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "服务指标")
public class ServiceMetricsVO {

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "QPS(每秒请求数)")
    private BigDecimal qps;

    @Schema(description = "响应时间P95(ms)")
    private BigDecimal p95ResponseTime;

    @Schema(description = "响应时间P99(ms)")
    private BigDecimal p99ResponseTime;

    @Schema(description = "平均响应时间(ms)")
    private BigDecimal avgResponseTime;

    @Schema(description = "错误率(%)")
    private BigDecimal errorRate;

    @Schema(description = "成功率(%)")
    private BigDecimal successRate;

    @Schema(description = "CPU使用率(%)")
    private BigDecimal cpuUsage;

    @Schema(description = "内存使用率(%)")
    private BigDecimal memoryUsage;

    @Schema(description = "JVM堆内存使用(MB)")
    private BigDecimal heapMemoryUsed;

    @Schema(description = "JVM堆内存最大(MB)")
    private BigDecimal heapMemoryMax;

    @Schema(description = "GC次数")
    private Long gcCount;

    @Schema(description = "GC耗时(ms)")
    private Long gcTime;

    @Schema(description = "线程数")
    private Integer threadCount;

    @Schema(description = "自定义指标")
    private Map<String, Object> customMetrics;
}
