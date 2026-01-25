package com.novax.data.sync.service;

import com.novax.data.sync.document.ChargingSessionDocument;
import com.novax.data.sync.repository.ChargingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 充电会话数据同步服务
 * 负责从MySQL同步充电会话数据到Elasticsearch
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionSyncService {

    private final JdbcTemplate jdbcTemplate;
    private final ChargingSessionRepository sessionRepository;

    private static final String BASE_SQL = """
            SELECT s.id, s.session_id, s.user_id, s.station_id, s.device_id,
                   s.device_code, s.energy, s.duration, s.total_amount, s.status,
                   s.start_time, s.end_time, s.create_time,
                   u.username as user_name,
                   st.station_name
            FROM charging_session s
            LEFT JOIN user u ON s.user_id = u.id
            LEFT JOIN charging_station st ON s.station_id = st.id
            """;

    /**
     * 增量同步充电会话数据
     * 每分钟执行一次，只同步最近1小时更新的数据
     */
    @Scheduled(fixedDelay = 60000)
    @Transactional(readOnly = true)
    public void syncRecentSessions() {
        log.info("开始增量同步充电会话数据");
        long startTime = System.currentTimeMillis();

        try {
            String sql = BASE_SQL + """
                    WHERE s.update_time >= DATE_SUB(NOW(), INTERVAL 1 HOUR)
                    ORDER BY s.update_time DESC
                    LIMIT 1000
                    """;

            List<ChargingSessionDocument> documents = jdbcTemplate.query(sql, new SessionRowMapper());

            if (!documents.isEmpty()) {
                sessionRepository.saveAll(documents);
                long elapsed = System.currentTimeMillis() - startTime;
                log.info("充电会话数据同步完成，共同步 {} 条记录，耗时 {} ms", documents.size(), elapsed);
            } else {
                log.debug("没有需要同步的充电会话数据");
            }

        } catch (Exception e) {
            log.error("充电会话数据同步失败", e);
            throw new RuntimeException("充电会话数据同步失败", e);
        }
    }

    /**
     * 同步单个会话
     * 用于实时同步单个会话的变更
     *
     * @param sessionId 会话ID
     * @return 是否同步成功
     */
    public boolean syncSession(Long sessionId) {
        if (sessionId == null) {
            log.warn("会话ID不能为空");
            return false;
        }

        log.info("同步充电会话: {}", sessionId);

        try {
            String sql = BASE_SQL + "WHERE s.id = ?";

            Optional<ChargingSessionDocument> docOpt = jdbcTemplate.query(
                    sql,
                    new SessionRowMapper(),
                    sessionId).stream().findFirst();

            if (docOpt.isPresent()) {
                sessionRepository.save(docOpt.get());
                log.info("充电会话同步完成: {}", sessionId);
                return true;
            } else {
                log.warn("未找到会话记录: {}", sessionId);
                return false;
            }

        } catch (EmptyResultDataAccessException e) {
            log.warn("会话不存在: {}", sessionId);
            return false;
        } catch (Exception e) {
            log.error("充电会话同步失败: {}", sessionId, e);
            return false;
        }
    }

    /**
     * 删除ES中的会话记录
     * 当MySQL中的会话被删除时调用
     *
     * @param sessionId 会话ID
     */
    public void deleteSession(Long sessionId) {
        if (sessionId == null) {
            log.warn("会话ID不能为空");
            return;
        }

        try {
            sessionRepository.deleteById(sessionId);
            log.info("已删除ES中的会话记录: {}", sessionId);
        } catch (Exception e) {
            log.error("删除ES中的会话记录失败: {}", sessionId, e);
        }
    }

    /**
     * 全量同步充电会话数据
     * 用于初始化或修复数据
     */
    @Transactional(readOnly = true)
    public void fullSync() {
        log.info("开始全量同步充电会话数据");
        long startTime = System.currentTimeMillis();

        try {
            String sql = BASE_SQL + "ORDER BY s.id";
            List<ChargingSessionDocument> documents = jdbcTemplate.query(sql, new SessionRowMapper());

            if (!documents.isEmpty()) {
                sessionRepository.saveAll(documents);
                long elapsed = System.currentTimeMillis() - startTime;
                log.info("充电会话全量同步完成，共同步 {} 条记录，耗时 {} ms", documents.size(), elapsed);
            }

        } catch (Exception e) {
            log.error("充电会话全量同步失败", e);
            throw new RuntimeException("充电会话全量同步失败", e);
        }
    }

    /**
     * RowMapper实现，将ResultSet映射为ChargingSessionDocument
     */
    private static class SessionRowMapper implements RowMapper<ChargingSessionDocument> {
        @Override
        public ChargingSessionDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChargingSessionDocument doc = new ChargingSessionDocument();

            doc.setId(rs.getLong("id"));
            doc.setSessionId(rs.getString("session_id"));

            long userId = rs.getLong("user_id");
            if (!rs.wasNull()) {
                doc.setUserId(userId);
            }
            doc.setUserName(rs.getString("user_name"));

            long stationId = rs.getLong("station_id");
            if (!rs.wasNull()) {
                doc.setStationId(stationId);
            }
            doc.setStationName(rs.getString("station_name"));

            long deviceId = rs.getLong("device_id");
            if (!rs.wasNull()) {
                doc.setDeviceId(deviceId);
            }
            doc.setDeviceCode(rs.getString("device_code"));

            BigDecimal energy = rs.getBigDecimal("energy");
            if (energy != null) {
                doc.setEnergy(energy);
            }

            int duration = rs.getInt("duration");
            if (!rs.wasNull()) {
                doc.setDuration(duration);
            }

            BigDecimal totalAmount = rs.getBigDecimal("total_amount");
            if (totalAmount != null) {
                doc.setTotalAmount(totalAmount);
            }

            doc.setStatus(rs.getString("status"));

            LocalDateTime startTime = rs.getObject("start_time", LocalDateTime.class);
            if (startTime != null) {
                doc.setStartTime(startTime);
            }

            LocalDateTime endTime = rs.getObject("end_time", LocalDateTime.class);
            if (endTime != null) {
                doc.setEndTime(endTime);
            }

            LocalDateTime createTime = rs.getObject("create_time", LocalDateTime.class);
            if (createTime != null) {
                doc.setCreateTime(createTime);
            }

            return doc;
        }
    }
}
