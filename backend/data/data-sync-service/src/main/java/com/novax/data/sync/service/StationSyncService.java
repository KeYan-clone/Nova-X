package com.novax.data.sync.service;

import com.novax.data.sync.document.ChargingStationDocument;
import com.novax.data.sync.repository.ChargingStationRepository;
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
 * 充电站数据同步服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StationSyncService {

    private final JdbcTemplate jdbcTemplate;
    private final ChargingStationRepository stationRepository;

    /**
     * 全量同步充电站数据
     * 每5分钟执行一次
     */
    @Scheduled(fixedDelayString = "${sync.sync-interval:300000}")
    @Transactional(readOnly = true)
    public void syncAllStations() {
        log.info("开始全量同步充电站数据");

        try {
            // 从MySQL读取充电站数据
            String sql = """
                SELECT s.id, s.station_name, s.address, s.latitude, s.longitude,
                       s.status, s.operator_name, s.facilities, s.create_time, s.update_time,
                       COUNT(d.id) as total_devices,
                       SUM(CASE WHEN d.status = 'AVAILABLE' THEN 1 ELSE 0 END) as available_devices
                FROM charging_station s
                LEFT JOIN charging_device d ON s.id = d.station_id
                GROUP BY s.id
                """;

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            List<ChargingStationDocument> documents = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                ChargingStationDocument doc = new ChargingStationDocument();
                doc.setId(((Number) row.get("id")).longValue());
                doc.setStationName((String) row.get("station_name"));
                doc.setAddress((String) row.get("address"));
                doc.setLatitude((BigDecimal) row.get("latitude"));
                doc.setLongitude((BigDecimal) row.get("longitude"));
                doc.setTotalDevices(((Number) row.get("total_devices")).intValue());
                doc.setAvailableDevices(((Number) row.get("available_devices")).intValue());
                doc.setStatus((String) row.get("status"));
                doc.setOperatorName((String) row.get("operator_name"));
                doc.setFacilities((String) row.get("facilities"));

                Object createTime = row.get("create_time");
                if (createTime instanceof LocalDateTime) {
                    doc.setCreateTime((LocalDateTime) createTime);
                }

                Object updateTime = row.get("update_time");
                if (updateTime instanceof LocalDateTime) {
                    doc.setUpdateTime((LocalDateTime) updateTime);
                }

                documents.add(doc);
            }

            // 批量保存到ES
            stationRepository.saveAll(documents);

            log.info("充电站数据同步完成，共同步 {} 条记录", documents.size());

        } catch (Exception e) {
            log.error("充电站数据同步失败", e);
        }
    }

    /**
     * 增量同步单个充电站
     */
    public void syncStation(Long stationId) {
        log.info("增量同步充电站: {}", stationId);

        try {
            String sql = """
                SELECT s.id, s.station_name, s.address, s.latitude, s.longitude,
                       s.status, s.operator_name, s.facilities, s.create_time, s.update_time,
                       COUNT(d.id) as total_devices,
                       SUM(CASE WHEN d.status = 'AVAILABLE' THEN 1 ELSE 0 END) as available_devices
                FROM charging_station s
                LEFT JOIN charging_device d ON s.id = d.station_id
                WHERE s.id = ?
                GROUP BY s.id
                """;

            Map<String, Object> row = jdbcTemplate.queryForMap(sql, stationId);

            ChargingStationDocument doc = new ChargingStationDocument();
            doc.setId(((Number) row.get("id")).longValue());
            doc.setStationName((String) row.get("station_name"));
            doc.setAddress((String) row.get("address"));
            doc.setLatitude((BigDecimal) row.get("latitude"));
            doc.setLongitude((BigDecimal) row.get("longitude"));
            doc.setTotalDevices(((Number) row.get("total_devices")).intValue());
            doc.setAvailableDevices(((Number) row.get("available_devices")).intValue());
            doc.setStatus((String) row.get("status"));
            doc.setOperatorName((String) row.get("operator_name"));
            doc.setFacilities((String) row.get("facilities"));

            Object createTime = row.get("create_time");
            if (createTime instanceof LocalDateTime) {
                doc.setCreateTime((LocalDateTime) createTime);
            }

            Object updateTime = row.get("update_time");
            if (updateTime instanceof LocalDateTime) {
                doc.setUpdateTime((LocalDateTime) updateTime);
            }

            stationRepository.save(doc);

            log.info("充电站增量同步完成: {}", stationId);

        } catch (Exception e) {
            log.error("充电站增量同步失败: {}", stationId, e);
        }
    }
}
