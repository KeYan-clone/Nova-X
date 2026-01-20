package com.novax.settlement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Schema(description = "创建结算单DTO")
public class CreateSettlementDTO {

    @Schema(description = "结算周期类型", required = true)
    @NotBlank(message = "结算周期类型不能为空")
    private String cycleType;

    @Schema(description = "结算开始日期", required = true)
    @NotNull(message = "结算开始日期不能为空")
    private LocalDate startDate;

    @Schema(description = "结算结束日期", required = true)
    @NotNull(message = "结算结束日期不能为空")
    private LocalDate endDate;

    @Schema(description = "充电站ID")
    private Long stationId;
}
