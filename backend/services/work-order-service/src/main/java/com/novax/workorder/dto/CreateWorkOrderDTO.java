package com.novax.workorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "创建工单DTO")
public class CreateWorkOrderDTO {

    @Schema(description = "工单类型", required = true)
    @NotBlank(message = "工单类型不能为空")
    private String orderType;

    @Schema(description = "设备ID", required = true)
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "故障描述", required = true)
    @NotBlank(message = "故障描述不能为空")
    private String faultDescription;

    @Schema(description = "优先级")
    private String priority;

    @Schema(description = "报修人ID")
    private Long reporterId;
}
