package com.novax.member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员信息VO
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@Schema(description = "会员信息VO")
public class MemberInfoVO {

    @Schema(description = "会员ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会员编号")
    private String memberNo;

    @Schema(description = "会员等级代码")
    private String levelCode;

    @Schema(description = "会员等级名称")
    private String levelName;

    @Schema(description = "当前积分")
    private Integer currentPoints;

    @Schema(description = "累计积分")
    private Integer totalPoints;

    @Schema(description = "账户余额")
    private BigDecimal balance;

    @Schema(description = "累计充电次数")
    private Integer chargingCount;

    @Schema(description = "累计充电电量")
    private BigDecimal totalChargingEnergy;

    @Schema(description = "累计消费金额")
    private BigDecimal totalSpent;

    @Schema(description = "会员状态")
    private String memberStatus;

    @Schema(description = "会员状态描述")
    private String memberStatusDesc;

    @Schema(description = "充电折扣率")
    private BigDecimal chargingDiscount;

    @Schema(description = "服务费折扣率")
    private BigDecimal serviceFeeDiscount;

    @Schema(description = "每月免费停车时长(分钟)")
    private Integer freeParkingMinutes;

    @Schema(description = "本月已用免费停车时长(分钟)")
    private Integer usedFreeParkingMinutes;

    @Schema(description = "积分倍率")
    private BigDecimal pointsMultiplier;

    @Schema(description = "会员开通时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime memberSince;

    @Schema(description = "会员到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
}
