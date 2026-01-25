package com.novax.billing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novax.billing.dto.BillQueryDTO;
import com.novax.billing.entity.Bill;
import com.novax.billing.mapper.BillMapper;
import com.novax.billing.service.BillService;
import com.novax.billing.vo.BillVO;
import com.novax.common.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 账单服务实现
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillMapper billMapper;

    private static final Map<String, String> BILL_STATUS_DESC = new HashMap<>();

    static {
        BILL_STATUS_DESC.put("UNPAID", "未支付");
        BILL_STATUS_DESC.put("PAID", "已支付");
        BILL_STATUS_DESC.put("REFUNDED", "已退款");
        BILL_STATUS_DESC.put("CANCELLED", "已取消");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Bill generateBillBySession(Long sessionId) {
        log.info("开始生成账单，会话ID: {}", sessionId);

        // TODO: 调用session-service获取充电会话信息
        // 这里使用模拟数据，实际应该通过Feign调用session-service

        // 模拟会话数据
        Map<String, Object> sessionData = mockSessionData(sessionId);

        // TODO: 调用pricing-service获取定价信息
        // 这里使用模拟数据，实际应该通过Feign调用pricing-service
        Map<String, Object> pricingData = mockPricingData();

        // 生成账单
        Bill bill = new Bill();
        bill.setBillNo(generateBillNo());
        bill.setSessionId(sessionId);
        bill.setUserId((Long) sessionData.get("userId"));
        bill.setStationId((Long) sessionData.get("stationId"));
        bill.setStationName((String) sessionData.get("stationName"));
        bill.setDeviceId((Long) sessionData.get("deviceId"));
        bill.setDeviceCode((String) sessionData.get("deviceCode"));
        bill.setConnectorId((Long) sessionData.get("connectorId"));
        bill.setConnectorCode((String) sessionData.get("connectorCode"));
        bill.setStartTime((LocalDateTime) sessionData.get("startTime"));
        bill.setEndTime((LocalDateTime) sessionData.get("endTime"));

        // 计算充电时长（分钟）
        Duration duration = Duration.between(bill.getStartTime(), bill.getEndTime());
        bill.setChargingDuration((int) duration.toMinutes());

        // 充电电量
        BigDecimal chargingEnergy = (BigDecimal) sessionData.get("chargingEnergy");
        bill.setChargingEnergy(chargingEnergy);

        // 定价模板ID
        bill.setPricingTemplateId((Long) pricingData.get("templateId"));

        // 计算费用
        calculateBillCost(bill, pricingData);

        // 设置账单状态
        bill.setBillStatus("UNPAID");

        // 保存账单
        billMapper.insert(bill);

        log.info("账单生成成功，账单编号: {}, 总金额: {}", bill.getBillNo(), bill.getTotalAmount());
        return bill;
    }

    /**
     * 计算账单费用
     */
    private void calculateBillCost(Bill bill, Map<String, Object> pricingData) {
        BigDecimal chargingEnergy = bill.getChargingEnergy();

        // 电费计算（实际应该根据分时电价计算）
        // 这里简化处理：使用平均电价
        BigDecimal avgPrice = (BigDecimal) pricingData.get("avgPrice");
        BigDecimal electricityCost = chargingEnergy.multiply(avgPrice)
                .setScale(2, RoundingMode.HALF_UP);
        bill.setElectricityCost(electricityCost);

        // 服务费计算（电费的10%）
        BigDecimal serviceFeeRate = (BigDecimal) pricingData.get("serviceFeeRate");
        BigDecimal serviceFee = chargingEnergy.multiply(serviceFeeRate)
                .setScale(2, RoundingMode.HALF_UP);
        bill.setServiceFee(serviceFee);

        // 停车费计算（超过2小时后，每小时5元）
        BigDecimal parkingFee = calculateParkingFee(bill.getChargingDuration());
        bill.setParkingFee(parkingFee);

        // 优惠金额（暂时为0）
        bill.setDiscountAmount(BigDecimal.ZERO);

        // 总金额 = 电费 + 服务费 + 停车费
        BigDecimal totalAmount = electricityCost.add(serviceFee).add(parkingFee);
        bill.setTotalAmount(totalAmount);

        // 实付金额 = 总金额 - 优惠金额
        BigDecimal paidAmount = totalAmount.subtract(bill.getDiscountAmount());
        bill.setPaidAmount(paidAmount);
    }

    /**
     * 计算停车费
     * 超过2小时后，每小时5元
     */
    private BigDecimal calculateParkingFee(Integer chargingDuration) {
        if (chargingDuration <= 120) { // 2小时内免费
            return BigDecimal.ZERO;
        }

        // 超出的分钟数
        int extraMinutes = chargingDuration - 120;
        // 向上取整到小时
        int extraHours = (int) Math.ceil(extraMinutes / 60.0);

        return BigDecimal.valueOf(extraHours * 5);
    }

    /**
     * 生成账单编号
     * 格式: BL+yyyyMMddHHmmss+3位随机数
     */
    private String generateBillNo() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%03d", new Random().nextInt(1000));
        return "BL" + timestamp + random;
    }

    @Override
    public BillVO getBillById(Long id) {
        Bill bill = billMapper.selectById(id);
        if (bill == null) {
            throw new BusinessException("账单不存在");
        }
        return convertToVO(bill);
    }

    @Override
    public BillVO getBillByBillNo(String billNo) {
        LambdaQueryWrapper<Bill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bill::getBillNo, billNo);
        Bill bill = billMapper.selectOne(wrapper);
        if (bill == null) {
            throw new BusinessException("账单不存在");
        }
        return convertToVO(bill);
    }

    @Override
    public Page<BillVO> queryBills(BillQueryDTO queryDTO) {
        Page<Bill> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<Bill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getBillNo() != null, Bill::getBillNo, queryDTO.getBillNo())
                .eq(queryDTO.getUserId() != null, Bill::getUserId, queryDTO.getUserId())
                .eq(queryDTO.getStationId() != null, Bill::getStationId, queryDTO.getStationId())
                .eq(queryDTO.getBillStatus() != null, Bill::getBillStatus, queryDTO.getBillStatus())
                .ge(queryDTO.getStartTime() != null, Bill::getStartTime, queryDTO.getStartTime())
                .le(queryDTO.getEndTime() != null, Bill::getEndTime, queryDTO.getEndTime())
                .orderByDesc(Bill::getStartTime);

        Page<Bill> billPage = billMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<BillVO> voPage = new Page<>();
        BeanUtils.copyProperties(billPage, voPage, "records");
        List<BillVO> voList = billPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean payBill(Long billId, String paymentMethod, String transactionNo) {
        Bill bill = billMapper.selectById(billId);
        if (bill == null) {
            throw new BusinessException("账单不存在");
        }

        if (!"UNPAID".equals(bill.getBillStatus())) {
            throw new BusinessException("账单状态不是未支付，无法支付");
        }

        bill.setBillStatus("PAID");
        bill.setPaymentMethod(paymentMethod);
        bill.setPaymentTime(LocalDateTime.now());
        bill.setPaymentTransactionNo(transactionNo);

        int rows = billMapper.updateById(bill);
        log.info("账单支付成功，账单ID: {}, 支付方式: {}, 支付流水号: {}",
                billId, paymentMethod, transactionNo);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelBill(Long billId) {
        Bill bill = billMapper.selectById(billId);
        if (bill == null) {
            throw new BusinessException("账单不存在");
        }

        if ("PAID".equals(bill.getBillStatus())) {
            throw new BusinessException("账单已支付，无法取消");
        }

        bill.setBillStatus("CANCELLED");
        int rows = billMapper.updateById(bill);
        log.info("账单取消成功，账单ID: {}", billId);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean refundBill(Long billId) {
        Bill bill = billMapper.selectById(billId);
        if (bill == null) {
            throw new BusinessException("账单不存在");
        }

        if (!"PAID".equals(bill.getBillStatus())) {
            throw new BusinessException("账单状态不是已支付，无法退款");
        }

        // TODO: 调用支付服务进行退款

        bill.setBillStatus("REFUNDED");
        int rows = billMapper.updateById(bill);
        log.info("账单退款成功，账单ID: {}", billId);
        return rows > 0;
    }

    /**
     * 转换为VO
     */
    private BillVO convertToVO(Bill bill) {
        BillVO vo = new BillVO();
        BeanUtils.copyProperties(bill, vo);
        vo.setBillStatusDesc(BILL_STATUS_DESC.get(bill.getBillStatus()));
        return vo;
    }

    /**
     * 模拟会话数据（实际应该通过Feign调用session-service）
     */
    private Map<String, Object> mockSessionData(Long sessionId) {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("userId", 1001L);
        data.put("stationId", 1L);
        data.put("stationName", "市中心充电站");
        data.put("deviceId", 1L);
        data.put("deviceCode", "DV20260120001");
        data.put("connectorId", 1L);
        data.put("connectorCode", "CNDV20260120001001");
        data.put("startTime", LocalDateTime.now().minusHours(2));
        data.put("endTime", LocalDateTime.now());
        data.put("chargingEnergy", new BigDecimal("35.68"));
        return data;
    }

    /**
     * 模拟定价数据（实际应该通过Feign调用pricing-service）
     */
    private Map<String, Object> mockPricingData() {
        Map<String, Object> data = new HashMap<>();
        data.put("templateId", 1L);
        data.put("avgPrice", new BigDecimal("0.80")); // 平均电价
        data.put("serviceFeeRate", new BigDecimal("0.10")); // 服务费率
        return data;
    }
}
