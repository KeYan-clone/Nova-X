package com.novax.session.service.impl;

import com.novax.common.core.exception.BusinessException;
import com.novax.common.core.result.ResultCode;
import com.novax.session.entity.ChargingSession;
import com.novax.session.mapper.SessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 充电会话服务实现
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl {

    private final SessionMapper sessionMapper;

    /**
     * 启动充电
     */
    @Transactional(rollbackFor = Exception.class)
    public Long startCharging(Long userId, Long connectorId) {
        // 生成会话编号
        String sessionCode = generateSessionCode();

        ChargingSession session = new ChargingSession();
        session.setSessionCode(sessionCode);
        session.setUserId(userId);
        session.setConnectorId(connectorId);
        session.setSessionStatus("CHARGING");
        session.setStartTime(LocalDateTime.now());
        session.setChargingEnergy(BigDecimal.ZERO);

        sessionMapper.insert(session);
        return session.getId();
    }

    /**
     * 停止充电
     */
    @Transactional(rollbackFor = Exception.class)
    public void stopCharging(Long sessionId, String stopReason) {
        ChargingSession session = sessionMapper.selectById(sessionId);
        if (session == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_FOUND, "会话不存在");
        }

        if (!"CHARGING".equals(session.getSessionStatus())) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "会话状态不正确");
        }

        session.setSessionStatus("COMPLETED");
        session.setEndTime(LocalDateTime.now());
        session.setStopReason(stopReason);

        sessionMapper.updateById(session);
    }

    /**
     * 生成会话编号
     */
    private String generateSessionCode() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "CS" + date + random;
    }
}
