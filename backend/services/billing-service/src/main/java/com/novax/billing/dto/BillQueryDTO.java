package com.novax.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 账单查询DTO
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@Schema(description = "账单查询DTO")
public class BillQueryDTO {

    @Schema(description = "账单编号")
    private String billNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "账单状态: UNPAID-未支付, PAID-已支付, REFUNDED-已退款, CANCELLED-已取消")
    private String billStatus;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;
}
