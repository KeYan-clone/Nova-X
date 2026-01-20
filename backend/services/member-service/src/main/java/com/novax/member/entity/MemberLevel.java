package com.novax.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员等级实体
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_level")
@Schema(description = "会员等级实体")
public class MemberLevel extends BaseEntity {

    @Schema(description = "等级名称", example = "黄金会员")
    private String levelName;

    @Schema(description = "等级代码", example = "GOLD")
    private String levelCode;

    @Schema(description = "等级", example = "3")
    private Integer level;

    @Schema(description = "升级所需积分", example = "5000")
    private Integer requiredPoints;

    @Schema(description = "充电折扣率 (0.1-1.0)", example = "0.95")
    private BigDecimal chargingDiscount;

    @Schema(description = "服务费折扣率 (0.1-1.0)", example = "0.90")
    private BigDecimal serviceFeeDiscount;

    @Schema(description = "每月免费停车时长(分钟)", example = "240")
    private Integer freeParkingMinutes;

    @Schema(description = "积分倍率", example = "1.5")
    private BigDecimal pointsMultiplier;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "等级描述")
    private String description;

    @Schema(description = "等级图标URL")
    private String iconUrl;
}
