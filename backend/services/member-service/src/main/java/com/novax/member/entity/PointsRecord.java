package com.novax.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 积分记录实体
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_record")
@Schema(description = "积分记录实体")
public class PointsRecord extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "积分变化类型: EARN-获得, CONSUME-消费, EXPIRE-过期, REFUND-退还")
    private String changeType;

    @Schema(description = "积分变化值", example = "100")
    private Integer points;

    @Schema(description = "变化后余额", example = "5280")
    private Integer balanceAfter;

    @Schema(description = "业务类型: CHARGING-充电, RECHARGE-充值, SIGNUP-注册, etc")
    private String businessType;

    @Schema(description = "关联业务ID")
    private Long businessId;

    @Schema(description = "关联金额(元)")
    private BigDecimal relatedAmount;

    @Schema(description = "积分来源描述")
    private String description;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;
}
