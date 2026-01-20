package com.novax.payment.service;

import com.novax.payment.dto.PaymentCreateDTO;
import com.novax.payment.dto.RefundApplyDTO;
import com.novax.payment.vo.PaymentResultVO;

/**
 * 支付服务接口
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
public interface PaymentService {

    /**
     * 创建支付
     *
     * @param createDTO 支付创建DTO
     * @return 支付结果
     */
    PaymentResultVO createPayment(PaymentCreateDTO createDTO);

    /**
     * 查询支付结果
     *
     * @param paymentNo 支付单号
     * @return 支付结果
     */
    PaymentResultVO queryPayment(String paymentNo);

    /**
     * 关闭支付
     *
     * @param paymentNo 支付单号
     * @return 是否成功
     */
    Boolean closePayment(String paymentNo);

    /**
     * 申请退款
     *
     * @param applyDTO 退款申请DTO
     * @return 是否成功
     */
    Boolean applyRefund(RefundApplyDTO applyDTO);

    /**
     * 查询退款结果
     *
     * @param refundNo 退款单号
     * @return 退款结果
     */
    Object queryRefund(String refundNo);
}
