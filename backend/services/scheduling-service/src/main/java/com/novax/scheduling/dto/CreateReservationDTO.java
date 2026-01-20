package com.novax.scheduling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "创建预约DTO")
public class CreateReservationDTO {

    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "充电站ID", required = true)
    @NotNull(message = "充电站ID不能为空")
    private Long stationId;

    @Schema(description = "预约开始时间", required = true)
    @NotNull(message = "预约开始时间不能为空")
    private LocalDateTime reservationStartTime;

    @Schema(description = "预约结束时间", required = true)
    @NotNull(message = "预约结束时间不能为空")
    private LocalDateTime reservationEndTime;

    @Schema(description = "预计充电电量")
    private BigDecimal estimatedEnergy;
}
