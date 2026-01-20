package com.novax.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.member.entity.BalanceRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 余额记录Mapper
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Mapper
public interface BalanceRecordMapper extends BaseMapper<BalanceRecord> {
}
