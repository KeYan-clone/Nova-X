package com.novax.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
@Schema(description = "发送通知DTO")
public class SendNotificationDTO {

    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "消息类型", required = true)
    @NotBlank(message = "消息类型不能为空")
    private String messageType;

    @Schema(description = "模板编码", required = true)
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    @Schema(description = "模板参数")
    private Map<String, String> params;

    @Schema(description = "接收地址")
    private String receiver;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "业务ID")
    private Long businessId;
}
