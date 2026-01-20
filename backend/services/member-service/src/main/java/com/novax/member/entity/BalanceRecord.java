package com.novax.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 余额记录实体
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("balance_record")
@Schema(description = "余额记录实体")
public class BalanceRecord extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "变化类型: RECHARGE-充值, CONSUME-消费, REFUND-退款, WITHDRAW-提现")
    private String changeType;

    @Schema(description = "变化金额(元)", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "变化后余额(元)", example = "258.50")
    private BigDecimal balanceAfter;

    @Schema(description = "业务类型: CHARGING-充电, MANUAL-手动, GIFT-赠送, etc")
    private String businessType;

    @Schema(description = "关联业务ID")
    private Long businessId;

    @Schema(description = "描述")
    private String description;
}
