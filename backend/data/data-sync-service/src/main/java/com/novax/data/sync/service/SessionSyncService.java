package com.novax.data.sync.service;

import com.novax.data.sync.document.ChargingSessionDocument;
import com.novax.data.sync.repository.ChargingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 充电会话数据同步服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionSyncService {

    private final JdbcTemplate jdbcTemplate;
    private final ChargingSessionRepository sessionRepository;

    /**
     * 增量同步充电会话数据
     * 只同步最近更新的数据（最近1小时）
     */
    @Scheduled(fixedDelay = 60000) // 每分钟执行一次
    @Transactional(readOnly = true)
    public void syncRecentSessions() {
        log.info("开始增量同步充电会话数据");

        try {
            // 只同步最近1小时更新的会话
            String sql = """
                SELECT s.id, s.session_id, s.user_id, s.station_id, s.device_id,
                       s.device_code, s.energy, s.duration, s.total_amount, s.status,
                       s.start_time, s.end_time, s.create_time,
                       u.username as user_name,
                       st.station_name
                FROM charging_session s
                LEFT JOIN user u ON s.user_id = u.id
                LEFT JOIN charging_station st ON s.station_id = st.id
                WHERE s.update_time >= DATE_SUB(NOW(), INTERVAL 1 HOUR)
                ORDER BY s.update_time DESC
                LIMIT 1000
                """;

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            List<ChargingSessionDocument> documents = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                ChargingSessionDocument doc = new ChargingSessionDocument();
                doc.setId(((Number) row.get("id")).longValue());
                doc.setSessionId((String) row.get("session_id"));

                Object userId = row.get("user_id");
                if (userId != null) {
                    doc.setUserId(((Number) userId).longValue());
                }
                doc.setUserName((String) row.get("user_name"));

                Object stationId = row.get("station_id");
                if (stationId != null) {
                    doc.setStationId(((Number) stationId).longValue());
                }
                doc.setStationName((String) row.get("station_name"));

                Object deviceId = row.get("device_id");
                if (deviceId != null) {
                    doc.setDeviceId(((Number) deviceId).longValue());
                }
                doc.setDeviceCode((String) row.get("device_code"));

                doc.setEnergy((BigDecimal) row.get("energy"));

                Object duration = row.get("duration");
                if (duration != null) {
                    doc.setDuration(((Number) duration).intValue());
                }

                doc.setTotalAmount((BigDecimal) row.get("total_amount"));
                doc.setStatus((String) row.get("status"));

                Object startTime = row.get("start_time");
                if (startTime instanceof LocalDateTime) {
                    doc.setStartTime((LocalDateTime) startTime);
                }

                Object endTime = row.get("end_time");
                if (endTime instanceof LocalDateTime) {
                    doc.setEndTime((LocalDateTime) endTime);
                }

                Object createTime = row.get("create_time");
                if (createTime instanceof LocalDateTime) {
                    doc.setCreateTime((LocalDateTime) createTime);
                }

                documents.add(doc);
            }

            if (!documents.isEmpty()) {
                sessionRepository.saveAll(documents);
                log.info("充电会话数据同步完成，共同步 {} 条记录", documents.size());
            }

        } catch (Exception e) {
            log.error("充电会话数据同步失败", e);
        }
    }

    /**
     * 同步单个会话
     */
    public void syncSession(Long sessionId) {
        log.info("同步充电会话: {}", sessionId);

        try {
            String sql = """
                SELECT s.id, s.session_id, s.user_id, s.station_id, s.device_id,
                       s.device_code, s.energy, s.duration, s.total_amount, s.status,
                       s.start_time, s.end_time, s.create_time,
                       u.username as user_name,
                       st.station_name
                FROM charging_session s
                LEFT JOIN user u ON s.user_id = u.id
                LEFT JOIN charging_station st ON s.station_id = st.id
                WHERE s.id = ?
                """;

            Map<String, Object> row = jdbcTemplate.queryForMap(sql, sessionId);

            ChargingSessionDocument doc = new ChargingSessionDocument();
            doc.setId(((Number) row.get("id")).longValue());
            doc.setSessionId((String) row.get("session_id"));

            Object userId = row.get("user_id");
            if (userId != null) {
                doc.setUserId(((Number) userId).longValue());
            }
            doc.setUserName((String) row.get("user_name"));

            Object stationId = row.get("station_id");
            if (stationId != null) {
                doc.setStationId(((Number) stationId).longValue());
            }
            doc.setStationName((String) row.get("station_name"));

            Object deviceId = row.get("device_id");
            if (deviceId != null) {
                doc.setDeviceId(((Number) deviceId).longValue());
            }
            doc.setDeviceCode((String) row.get("device_code"));

            doc.setEnergy((BigDecimal) row.get("energy"));

            Object duration = row.get("duration");
            if (duration != null) {
                doc.setDuration(((Number) duration).intValue());
            }

            doc.setTotalAmount((BigDecimal) row.get("total_amount"));
            doc.setStatus((String) row.get("status"));

            Object startTime = row.get("start_time");
            if (startTime instanceof LocalDateTime) {
                doc.setStartTime((LocalDateTime) startTime);
            }

            Object endTime = row.get("end_time");
            if (endTime instanceof LocalDateTime) {
                doc.setEndTime((LocalDateTime) endTime);
            }

            Object createTime = row.get("create_time");
            if (createTime instanceof LocalDateTime) {
                doc.setCreateTime((LocalDateTime) createTime);
            }

            sessionRepository.save(doc);

            log.info("充电会话同步完成: {}", sessionId);

        } catch (Exception e) {
            log.error("充电会话同步失败: {}", sessionId, e);
        }
    }
}
