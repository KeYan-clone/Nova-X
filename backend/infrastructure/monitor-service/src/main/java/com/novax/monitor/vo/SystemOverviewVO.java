package com.novax.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统概览VO
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统概览")
public class SystemOverviewVO {

    @Schema(description = "服务总数")
    private Integer totalServices;

    @Schema(description = "健康服务数")
    private Integer healthyServices;

    @Schema(description = "异常服务数")
    private Integer unhealthyServices;

    @Schema(description = "总QPS")
    private Long totalQps;

    @Schema(description = "平均响应时间(ms)")
    private Long avgResponseTime;

    @Schema(description = "错误率(%)")
    private Double errorRate;

    @Schema(description = "今日请求总数")
    private Long todayTotalRequests;

    @Schema(description = "今日错误数")
    private Long todayErrors;

    @Schema(description = "服务健康列表")
    private List<ServiceHealthVO> serviceHealthList;
}
