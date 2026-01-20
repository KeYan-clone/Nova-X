package com.novax.session.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.session.entity.ChargingSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话Mapper
 */
@Mapper
public interface SessionMapper extends BaseMapper<ChargingSession> {
}
