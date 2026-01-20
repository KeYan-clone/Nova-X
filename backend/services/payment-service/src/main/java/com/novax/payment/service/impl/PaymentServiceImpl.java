package com.novax.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novax.common.core.exception.BusinessException;
import com.novax.payment.dto.PaymentCreateDTO;
import com.novax.payment.dto.RefundApplyDTO;
import com.novax.payment.entity.PaymentRecord;
import com.novax.payment.entity.RefundRecord;
import com.novax.payment.mapper.PaymentMapper;
import com.novax.payment.mapper.RefundMapper;
import com.novax.payment.service.PaymentService;
import com.novax.payment.vo.PaymentResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 支付服务实现
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final RefundMapper refundMapper;

    private static final Map<String, String> PAYMENT_STATUS_DESC = new HashMap<>();

    static {
        PAYMENT_STATUS_DESC.put("PENDING", "待支付");
        PAYMENT_STATUS_DESC.put("PAYING", "支付中");
        PAYMENT_STATUS_DESC.put("SUCCESS", "支付成功");
        PAYMENT_STATUS_DESC.put("FAILED", "支付失败");
        PAYMENT_STATUS_DESC.put("CLOSED", "已关闭");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResultVO createPayment(PaymentCreateDTO createDTO) {
        log.info("创建支付, 账单ID: {}, 金额: {}, 支付方式: {}",
                createDTO.getBillId(), createDTO.getPaymentAmount(), createDTO.getPaymentMethod());

        // TODO: 调用billing-service获取账单信息验证

        // 创建支付记录
        PaymentRecord record = new PaymentRecord();
        record.setPaymentNo(generatePaymentNo());
        record.setBillId(createDTO.getBillId());
        record.setBillNo("BL" + createDTO.getBillId()); // 模拟账单编号
        record.setUserId(1001L); // TODO: 从上下文获取用户ID
        record.setPaymentAmount(createDTO.getPaymentAmount());
        record.setPaymentMethod(createDTO.getPaymentMethod());
        record.setPaymentChannel(createDTO.getPaymentChannel());
        record.setPaymentStatus("PENDING");
        record.setClientIp(createDTO.getClientIp());
        record.setDeviceInfo(createDTO.getDeviceInfo());
        record.setNotifyCount(0);

        // 保存支付记录
        paymentMapper.insert(record);

        // 调用第三方支付
        Map<String, Object> paymentResult = callThirdPayment(record);

        // 更新支付记录
        record.setThirdPaymentNo((String) paymentResult.get("thirdPaymentNo"));
        record.setPaymentStatus("PAYING");
        paymentMapper.updateById(record);

        // 构造返回结果
        PaymentResultVO resultVO = new PaymentResultVO();
        BeanUtils.copyProperties(record, resultVO);
        resultVO.setPaymentId(record.getId());
        resultVO.setPaymentStatusDesc(PAYMENT_STATUS_DESC.get(record.getPaymentStatus()));
        resultVO.setPaymentUrl((String) paymentResult.get("paymentUrl"));
        resultVO.setPaymentQrCode((String) paymentResult.get("paymentQrCode"));

        log.info("支付创建成功, 支付单号: {}", record.getPaymentNo());
        return resultVO;
    }

    @Override
    public PaymentResultVO queryPayment(String paymentNo) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getPaymentNo, paymentNo);
        PaymentRecord record = paymentMapper.selectOne(wrapper);

        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }

        // TODO: 调用第三方支付查询接口获取最新状态

        PaymentResultVO resultVO = new PaymentResultVO();
        BeanUtils.copyProperties(record, resultVO);
        resultVO.setPaymentId(record.getId());
        resultVO.setPaymentStatusDesc(PAYMENT_STATUS_DESC.get(record.getPaymentStatus()));

        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean closePayment(String paymentNo) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getPaymentNo, paymentNo);
        PaymentRecord record = paymentMapper.selectOne(wrapper);

        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }

        if ("SUCCESS".equals(record.getPaymentStatus())) {
            throw new BusinessException("支付已成功，无法关闭");
        }

        // TODO: 调用第三方支付关闭接口

        record.setPaymentStatus("CLOSED");
        int rows = paymentMapper.updateById(record);

        log.info("支付关闭成功, 支付单号: {}", paymentNo);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean applyRefund(RefundApplyDTO applyDTO) {
        log.info("申请退款, 支付ID: {}, 退款金额: {}", applyDTO.getPaymentId(), applyDTO.getRefundAmount());

        // 查询支付记录
        PaymentRecord paymentRecord = paymentMapper.selectById(applyDTO.getPaymentId());
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        if (!"SUCCESS".equals(paymentRecord.getPaymentStatus())) {
            throw new BusinessException("支付状态不是成功，无法退款");
        }

        if (applyDTO.getRefundAmount().compareTo(paymentRecord.getPaymentAmount()) > 0) {
            throw new BusinessException("退款金额不能大于支付金额");
        }

        // 创建退款记录
        RefundRecord refundRecord = new RefundRecord();
        refundRecord.setRefundNo(generateRefundNo());
        refundRecord.setPaymentId(applyDTO.getPaymentId());
        refundRecord.setPaymentNo(paymentRecord.getPaymentNo());
        refundRecord.setBillId(paymentRecord.getBillId());
        refundRecord.setUserId(paymentRecord.getUserId());
        refundRecord.setRefundAmount(applyDTO.getRefundAmount());
        refundRecord.setRefundReason(applyDTO.getRefundReason());
        refundRecord.setRefundStatus("PENDING");

        refundMapper.insert(refundRecord);

        // TODO: 调用第三方退款接口

        refundRecord.setRefundStatus("PROCESSING");
        refundRecord.setThirdRefundNo("THIRD_REF_" + System.currentTimeMillis());
        refundMapper.updateById(refundRecord);

        log.info("退款申请成功, 退款单号: {}", refundRecord.getRefundNo());
        return true;
    }

    @Override
    public Object queryRefund(String refundNo) {
        LambdaQueryWrapper<RefundRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundRecord::getRefundNo, refundNo);
        RefundRecord record = refundMapper.selectOne(wrapper);

        if (record == null) {
            throw new BusinessException("退款记录不存在");
        }

        // TODO: 调用第三方退款查询接口获取最新状态

        return record;
    }

    /**
     * 生成支付单号
     * 格式: PAY+yyyyMMddHHmmss+3位随机数
     */
    private String generatePaymentNo() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%03d", new Random().nextInt(1000));
        return "PAY" + timestamp + random;
    }

    /**
     * 生成退款单号
     * 格式: REF+yyyyMMddHHmmss+3位随机数
     */
    private String generateRefundNo() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%03d", new Random().nextInt(1000));
        return "REF" + timestamp + random;
    }

    /**
     * 调用第三方支付（模拟）
     */
    private Map<String, Object> callThirdPayment(PaymentRecord record) {
        Map<String, Object> result = new HashMap<>();

        // 模拟第三方支付返回
        String thirdPaymentNo = "THIRD_PAY_" + System.currentTimeMillis();
        result.put("thirdPaymentNo", thirdPaymentNo);

        // 根据不同支付方式返回不同内容
        switch (record.getPaymentMethod()) {
            case "WECHAT":
                result.put("paymentUrl", "weixin://wxpay/bizpayurl?pr=xxxxx");
                result.put("paymentQrCode", "data:image/png;base64,iVBORw0KG...");
                break;
            case "ALIPAY":
                result.put("paymentUrl", "https://mapi.alipay.com/gateway.do?xxx");
                result.put("paymentQrCode", "https://qr.alipay.com/xxx");
                break;
            case "UNIONPAY":
                result.put("paymentUrl", "https://gateway.95516.com/xxx");
                break;
            default:
                break;
        }

        return result;
    }
}
