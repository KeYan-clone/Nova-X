package com.novax.member.service;

import com.novax.member.dto.RechargeDTO;
import com.novax.member.vo.MemberInfoVO;

import java.math.BigDecimal;

/**
 * 会员服务接口
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
public interface MemberService {

    /**
     * 开通会员
     *
     * @param userId 用户ID
     * @return 会员信息
     */
    MemberInfoVO openMembership(Long userId);

    /**
     * 根据用户ID查询会员信息
     *
     * @param userId 用户ID
     * @return 会员信息
     */
    MemberInfoVO getMemberByUserId(Long userId);

    /**
     * 余额充值
     *
     * @param userId 用户ID
     * @param rechargeDTO 充值信息
     * @return 是否成功
     */
    Boolean recharge(Long userId, RechargeDTO rechargeDTO);

    /**
     * 余额消费
     *
     * @param userId 用户ID
     * @param amount 消费金额
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 是否成功
     */
    Boolean consumeBalance(Long userId, BigDecimal amount, String businessType, Long businessId);

    /**
     * 增加积分
     *
     * @param userId 用户ID
     * @param points 积分值
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 是否成功
     */
    Boolean addPoints(Long userId, Integer points, String businessType, Long businessId);

    /**
     * 消费积分
     *
     * @param userId 用户ID
     * @param points 积分值
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 是否成功
     */
    Boolean consumePoints(Long userId, Integer points, String businessType, Long businessId);

    /**
     * 检查并升级会员等级
     *
     * @param userId 用户ID
     */
    void checkAndUpgradeLevel(Long userId);
}
