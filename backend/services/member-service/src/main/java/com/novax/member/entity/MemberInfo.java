package com.novax.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员信息实体
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_info")
@Schema(description = "会员信息实体")
public class MemberInfo extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会员编号", example = "MB20260120001")
    private String memberNo;

    @Schema(description = "会员等级ID")
    private Long levelId;

    @Schema(description = "会员等级代码", example = "GOLD")
    private String levelCode;

    @Schema(description = "当前积分", example = "5280")
    private Integer currentPoints;

    @Schema(description = "累计积分", example = "12680")
    private Integer totalPoints;

    @Schema(description = "账户余额(元)", example = "258.50")
    private BigDecimal balance;

    @Schema(description = "累计充电次数", example = "136")
    private Integer chargingCount;

    @Schema(description = "累计充电电量(kWh)", example = "3568.90")
    private BigDecimal totalChargingEnergy;

    @Schema(description = "累计消费金额(元)", example = "5280.50")
    private BigDecimal totalSpent;

    @Schema(description = "会员状态: NORMAL-正常, FROZEN-冻结, CANCELLED-已注销")
    private String memberStatus;

    @Schema(description = "会员开通时间")
    private LocalDateTime memberSince;

    @Schema(description = "会员到期时间")
    private LocalDateTime expireTime;

    @Schema(description = "本月已用免费停车时长(分钟)")
    private Integer usedFreeParkingMinutes;
}
