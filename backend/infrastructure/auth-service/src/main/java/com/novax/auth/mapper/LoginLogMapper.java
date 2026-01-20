package com.novax.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.auth.domain.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志Mapper
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
}
