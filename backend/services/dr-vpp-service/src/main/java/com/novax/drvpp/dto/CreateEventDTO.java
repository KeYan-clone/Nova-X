package com.novax.drvpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "创建需求响应事件DTO")
public class CreateEventDTO {

    @Schema(description = "事件名称", required = true)
    @NotBlank(message = "事件名称不能为空")
    private String eventName;

    @Schema(description = "事件类型", required = true)
    @NotBlank(message = "事件类型不能为空")
    private String eventType;

    @Schema(description = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "目标削减功率", required = true)
    @NotNull(message = "目标削减功率不能为空")
    private BigDecimal targetPowerReduction;

    @Schema(description = "激励单价", required = true)
    @NotNull(message = "激励单价不能为空")
    private BigDecimal incentivePrice;

    @Schema(description = "事件描述")
    private String description;
}
