package com.novax.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.payment.entity.RefundRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款记录Mapper
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Mapper
public interface RefundMapper extends BaseMapper<RefundRecord> {
}
