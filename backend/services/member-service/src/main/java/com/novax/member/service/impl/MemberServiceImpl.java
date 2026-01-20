package com.novax.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novax.common.core.exception.BusinessException;
import com.novax.member.dto.RechargeDTO;
import com.novax.member.entity.*;
import com.novax.member.mapper.*;
import com.novax.member.service.MemberService;
import com.novax.member.vo.MemberInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 会员服务实现
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberInfoMapper memberInfoMapper;
    private final MemberLevelMapper memberLevelMapper;
    private final PointsRecordMapper pointsRecordMapper;
    private final BalanceRecordMapper balanceRecordMapper;

    private static final Map<String, String> MEMBER_STATUS_DESC = new HashMap<>();

    static {
        MEMBER_STATUS_DESC.put("NORMAL", "正常");
        MEMBER_STATUS_DESC.put("FROZEN", "冻结");
        MEMBER_STATUS_DESC.put("CANCELLED", "已注销");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberInfoVO openMembership(Long userId) {
        log.info("开通会员, 用户ID: {}", userId);

        // 检查是否已开通
        LambdaQueryWrapper<MemberInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberInfo::getUserId, userId);
        MemberInfo existingMember = memberInfoMapper.selectOne(wrapper);
        if (existingMember != null) {
            throw new BusinessException("用户已是会员");
        }

        // 获取初始等级（普通会员）
        LambdaQueryWrapper<MemberLevel> levelWrapper = new LambdaQueryWrapper<>();
        levelWrapper.eq(MemberLevel::getLevelCode, "BRONZE")
                .eq(MemberLevel::getEnabled, true);
        MemberLevel level = memberLevelMapper.selectOne(levelWrapper);
        if (level == null) {
            throw new BusinessException("会员等级配置错误");
        }

        // 创建会员
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUserId(userId);
        memberInfo.setMemberNo(generateMemberNo());
        memberInfo.setLevelId(level.getId());
        memberInfo.setLevelCode(level.getLevelCode());
        memberInfo.setCurrentPoints(0);
        memberInfo.setTotalPoints(0);
        memberInfo.setBalance(BigDecimal.ZERO);
        memberInfo.setChargingCount(0);
        memberInfo.setTotalChargingEnergy(BigDecimal.ZERO);
        memberInfo.setTotalSpent(BigDecimal.ZERO);
        memberInfo.setMemberStatus("NORMAL");
        memberInfo.setMemberSince(LocalDateTime.now());
        memberInfo.setUsedFreeParkingMinutes(0);

        memberInfoMapper.insert(memberInfo);

        // 赠送注册积分
        addPoints(userId, 100, "SIGNUP", memberInfo.getId());

        log.info("会员开通成功, 会员编号: {}", memberInfo.getMemberNo());
        return convertToVO(memberInfo, level);
    }

    @Override
    public MemberInfoVO getMemberByUserId(Long userId) {
        LambdaQueryWrapper<MemberInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberInfo::getUserId, userId);
        MemberInfo memberInfo = memberInfoMapper.selectOne(wrapper);

        if (memberInfo == null) {
            throw new BusinessException("用户不是会员");
        }

        // 获取等级信息
        MemberLevel level = memberLevelMapper.selectById(memberInfo.getLevelId());

        return convertToVO(memberInfo, level);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean recharge(Long userId, RechargeDTO rechargeDTO) {
        log.info("余额充值, 用户ID: {}, 金额: {}", userId, rechargeDTO.getAmount());

        MemberInfo memberInfo = getMemberInfoByUserId(userId);

        // TODO: 调用支付服务进行支付

        // 更新余额
        BigDecimal newBalance = memberInfo.getBalance().add(rechargeDTO.getAmount());
        memberInfo.setBalance(newBalance);
        memberInfoMapper.updateById(memberInfo);

        // 记录余额变动
        BalanceRecord record = new BalanceRecord();
        record.setUserId(userId);
        record.setMemberId(memberInfo.getId());
        record.setChangeType("RECHARGE");
        record.setAmount(rechargeDTO.getAmount());
        record.setBalanceAfter(newBalance);
        record.setBusinessType("RECHARGE");
        record.setDescription("余额充值");
        balanceRecordMapper.insert(record);

        // 赠送积分（充值1元=1积分）
        int points = rechargeDTO.getAmount().intValue();
        addPoints(userId, points, "RECHARGE", record.getId());

        log.info("充值成功, 当前余额: {}", newBalance);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean consumeBalance(Long userId, BigDecimal amount, String businessType, Long businessId) {
        MemberInfo memberInfo = getMemberInfoByUserId(userId);

        if (memberInfo.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }

        // 更新余额
        BigDecimal newBalance = memberInfo.getBalance().subtract(amount);
        memberInfo.setBalance(newBalance);
        memberInfoMapper.updateById(memberInfo);

        // 记录余额变动
        BalanceRecord record = new BalanceRecord();
        record.setUserId(userId);
        record.setMemberId(memberInfo.getId());
        record.setChangeType("CONSUME");
        record.setAmount(amount);
        record.setBalanceAfter(newBalance);
        record.setBusinessType(businessType);
        record.setBusinessId(businessId);
        record.setDescription("余额消费");
        balanceRecordMapper.insert(record);

        log.info("余额消费成功, 用户ID: {}, 金额: {}, 剩余: {}", userId, amount, newBalance);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addPoints(Long userId, Integer points, String businessType, Long businessId) {
        MemberInfo memberInfo = getMemberInfoByUserId(userId);

        // 获取积分倍率
        MemberLevel level = memberLevelMapper.selectById(memberInfo.getLevelId());
        if (level != null && level.getPointsMultiplier() != null) {
            points = (int) (points * level.getPointsMultiplier().doubleValue());
        }

        // 更新积分
        Integer newPoints = memberInfo.getCurrentPoints() + points;
        Integer newTotalPoints = memberInfo.getTotalPoints() + points;
        memberInfo.setCurrentPoints(newPoints);
        memberInfo.setTotalPoints(newTotalPoints);
        memberInfoMapper.updateById(memberInfo);

        // 记录积分变动
        PointsRecord record = new PointsRecord();
        record.setUserId(userId);
        record.setMemberId(memberInfo.getId());
        record.setChangeType("EARN");
        record.setPoints(points);
        record.setBalanceAfter(newPoints);
        record.setBusinessType(businessType);
        record.setBusinessId(businessId);
        record.setDescription("积分获得");
        record.setExpireTime(LocalDateTime.now().plusYears(1)); // 1年后过期
        pointsRecordMapper.insert(record);

        // 检查是否需要升级
        checkAndUpgradeLevel(userId);

        log.info("积分增加成功, 用户ID: {}, 积分: {}, 当前: {}", userId, points, newPoints);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean consumePoints(Long userId, Integer points, String businessType, Long businessId) {
        MemberInfo memberInfo = getMemberInfoByUserId(userId);

        if (memberInfo.getCurrentPoints() < points) {
            throw new BusinessException("积分不足");
        }

        // 更新积分
        Integer newPoints = memberInfo.getCurrentPoints() - points;
        memberInfo.setCurrentPoints(newPoints);
        memberInfoMapper.updateById(memberInfo);

        // 记录积分变动
        PointsRecord record = new PointsRecord();
        record.setUserId(userId);
        record.setMemberId(memberInfo.getId());
        record.setChangeType("CONSUME");
        record.setPoints(-points);
        record.setBalanceAfter(newPoints);
        record.setBusinessType(businessType);
        record.setBusinessId(businessId);
        record.setDescription("积分消费");
        pointsRecordMapper.insert(record);

        log.info("积分消费成功, 用户ID: {}, 积分: {}, 剩余: {}", userId, points, newPoints);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkAndUpgradeLevel(Long userId) {
        MemberInfo memberInfo = getMemberInfoByUserId(userId);

        // 查询所有等级，按等级从高到低排序
        LambdaQueryWrapper<MemberLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberLevel::getEnabled, true)
                .orderByDesc(MemberLevel::getLevel);
        List<MemberLevel> levels = memberLevelMapper.selectList(wrapper);

        // 找到符合条件的最高等级
        for (MemberLevel level : levels) {
            if (memberInfo.getTotalPoints() >= level.getRequiredPoints()
                    && level.getLevel() > getCurrentLevel(memberInfo.getLevelCode())) {
                // 升级
                memberInfo.setLevelId(level.getId());
                memberInfo.setLevelCode(level.getLevelCode());
                memberInfoMapper.updateById(memberInfo);
                log.info("会员升级, 用户ID: {}, 新等级: {}", userId, level.getLevelName());
                break;
            }
        }
    }

    /**
     * 获取当前等级数字
     */
    private Integer getCurrentLevel(String levelCode) {
        LambdaQueryWrapper<MemberLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberLevel::getLevelCode, levelCode);
        MemberLevel level = memberLevelMapper.selectOne(wrapper);
        return level != null ? level.getLevel() : 0;
    }

    /**
     * 根据用户ID获取会员信息
     */
    private MemberInfo getMemberInfoByUserId(Long userId) {
        LambdaQueryWrapper<MemberInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberInfo::getUserId, userId);
        MemberInfo memberInfo = memberInfoMapper.selectOne(wrapper);

        if (memberInfo == null) {
            throw new BusinessException("用户不是会员");
        }

        return memberInfo;
    }

    /**
     * 生成会员编号
     * 格式: MB+yyyyMMdd+4位序号
     */
    private String generateMemberNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "MB" + date + random;
    }

    /**
     * 转换为VO
     */
    private MemberInfoVO convertToVO(MemberInfo memberInfo, MemberLevel level) {
        MemberInfoVO vo = new MemberInfoVO();
        BeanUtils.copyProperties(memberInfo, vo);

        if (level != null) {
            vo.setLevelName(level.getLevelName());
            vo.setChargingDiscount(level.getChargingDiscount());
            vo.setServiceFeeDiscount(level.getServiceFeeDiscount());
            vo.setFreeParkingMinutes(level.getFreeParkingMinutes());
            vo.setPointsMultiplier(level.getPointsMultiplier());
        }

        vo.setMemberStatusDesc(MEMBER_STATUS_DESC.get(memberInfo.getMemberStatus()));

        return vo;
    }
}
