package com.novax.billing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novax.billing.dto.BillQueryDTO;
import com.novax.billing.entity.Bill;
import com.novax.billing.vo.BillVO;

/**
 * 账单服务接口
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
public interface BillService {

    /**
     * 根据会话ID生成账单
     *
     * @param sessionId 会话ID
     * @return 账单
     */
    Bill generateBillBySession(Long sessionId);

    /**
     * 根据ID查询账单
     *
     * @param id 账单ID
     * @return 账单VO
     */
    BillVO getBillById(Long id);

    /**
     * 根据账单编号查询账单
     *
     * @param billNo 账单编号
     * @return 账单VO
     */
    BillVO getBillByBillNo(String billNo);

    /**
     * 分页查询账单
     *
     * @param queryDTO 查询条件
     * @return 账单分页列表
     */
    Page<BillVO> queryBills(BillQueryDTO queryDTO);

    /**
     * 支付账单
     *
     * @param billId 账单ID
     * @param paymentMethod 支付方式
     * @param transactionNo 支付流水号
     * @return 是否成功
     */
    Boolean payBill(Long billId, String paymentMethod, String transactionNo);

    /**
     * 取消账单
     *
     * @param billId 账单ID
     * @return 是否成功
     */
    Boolean cancelBill(Long billId);

    /**
     * 退款
     *
     * @param billId 账单ID
     * @return 是否成功
     */
    Boolean refundBill(Long billId);
}
