package com.novax.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.payment.entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付记录Mapper
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Mapper
public interface PaymentMapper extends BaseMapper<PaymentRecord> {
}
