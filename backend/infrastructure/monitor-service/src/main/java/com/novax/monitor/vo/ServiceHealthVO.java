package com.novax.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务健康状态VO
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "服务健康状态")
public class ServiceHealthVO {

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "服务实例ID")
    private String instanceId;

    @Schema(description = "服务地址")
    private String address;

    @Schema(description = "健康状态：UP, DOWN, UNKNOWN")
    private String status;

    @Schema(description = "响应时间(ms)")
    private Long responseTime;

    @Schema(description = "错误信息")
    private String error;

    @Schema(description = "最后检查时间")
    private String lastCheckTime;
}
