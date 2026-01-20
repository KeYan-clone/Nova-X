package com.novax.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novax.common.core.exception.BusinessException;
import com.novax.settlement.dto.CreateSettlementDTO;
import com.novax.settlement.entity.SettlementOrder;
import com.novax.settlement.mapper.SettlementOrderMapper;
import com.novax.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final SettlementOrderMapper settlementOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettlementOrder createSettlement(CreateSettlementDTO dto) {
        log.info("创建结算单, 周期: {}, 日期: {} - {}",
                dto.getCycleType(), dto.getStartDate(), dto.getEndDate());

        BigDecimal totalAmount = new BigDecimal("10000.00");
        BigDecimal platformFee = totalAmount.multiply(new BigDecimal("0.05"));
        BigDecimal electricityCost = totalAmount.multiply(new BigDecimal("0.60"));
        BigDecimal stationShare = totalAmount.subtract(platformFee).subtract(electricityCost);

        SettlementOrder order = new SettlementOrder();
        order.setSettlementNo(generateSettlementNo());
        order.setCycleType(dto.getCycleType());
        order.setStartDate(dto.getStartDate());
        order.setEndDate(dto.getEndDate());
        order.setStationId(dto.getStationId() != null ? dto.getStationId() : 1L);
        order.setStationName("市中心充电站");
        order.setTotalAmount(totalAmount);
        order.setPlatformFee(platformFee);
        order.setElectricityCost(electricityCost);
        order.setStationShare(stationShare);
        order.setSettlementStatus("CALCULATED");
        order.setOrderCount(200);

        settlementOrderMapper.insert(order);

        log.info("结算单创建成功, 编号: {}", order.getSettlementNo());
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmSettlement(Long settlementId) {
        SettlementOrder order = settlementOrderMapper.selectById(settlementId);
        if (order == null) {
            throw new BusinessException("结算单不存在");
        }

        if (!"CALCULATED".equals(order.getSettlementStatus())) {
            throw new BusinessException("结算单状态不正确");
        }

        order.setSettlementStatus("CONFIRMED");
        order.setConfirmTime(LocalDateTime.now());

        settlementOrderMapper.updateById(order);
        log.info("结算单确认成功, ID: {}", settlementId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean paySettlement(Long settlementId) {
        SettlementOrder order = settlementOrderMapper.selectById(settlementId);
        if (order == null) {
            throw new BusinessException("结算单不存在");
        }

        if (!"CONFIRMED".equals(order.getSettlementStatus())) {
            throw new BusinessException("结算单未确认");
        }

        order.setSettlementStatus("PAID");
        order.setPayTime(LocalDateTime.now());

        settlementOrderMapper.updateById(order);
        log.info("结算单支付成功, ID: {}", settlementId);
        return true;
    }

    @Override
    public SettlementOrder getById(Long settlementId) {
        return settlementOrderMapper.selectById(settlementId);
    }

    @Override
    public List<SettlementOrder> getPendingSettlements() {
        LambdaQueryWrapper<SettlementOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SettlementOrder::getSettlementStatus, "CALCULATED", "CONFIRMED")
                .orderByAsc(SettlementOrder::getStartDate);
        return settlementOrderMapper.selectList(wrapper);
    }

    private String generateSettlementNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%03d", new Random().nextInt(1000));
        return "ST" + timestamp + random;
    }
}
