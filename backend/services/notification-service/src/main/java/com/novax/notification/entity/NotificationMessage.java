package com.novax.notification.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("notification_message")
@Schema(description = "通知消息")
public class NotificationMessage extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "消息类型: SMS-短信, EMAIL-邮件, PUSH-推送, IN_APP-站内信")
    private String messageType;

    @Schema(description = "消息模板ID")
    private Long templateId;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "接收地址(手机号/邮箱)")
    private String receiver;

    @Schema(description = "业务类型: CHARGING-充电, PAYMENT-支付, SYSTEM-系统")
    private String businessType;

    @Schema(description = "业务ID")
    private Long businessId;

    @Schema(description = "发送状态: PENDING-待发送, SENDING-发送中, SUCCESS-成功, FAILED-失败")
    private String sendStatus;

    @Schema(description = "发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "失败原因")
    private String failureReason;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "是否已读")
    private Boolean isRead;

    @Schema(description = "读取时间")
    private LocalDateTime readTime;
}
