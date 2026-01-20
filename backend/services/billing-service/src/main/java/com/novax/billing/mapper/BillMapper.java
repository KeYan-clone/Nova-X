package com.novax.billing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.billing.entity.Bill;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账单Mapper
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Mapper
public interface BillMapper extends BaseMapper<Bill> {
}
