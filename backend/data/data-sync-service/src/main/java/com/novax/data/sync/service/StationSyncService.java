package com.novax.data.sync.service;

import com.novax.data.sync.document.ChargingStationDocument;
import com.novax.data.sync.repository.ChargingStationRepository;
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
 * 充电站数据同步服务
 * 负责从MySQL同步充电站数据到Elasticsearch
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StationSyncService {

    private final JdbcTemplate jdbcTemplate;
    private final ChargingStationRepository stationRepository;

    private static final String BASE_SQL = """
            SELECT s.id, s.station_name, s.address, s.latitude, s.longitude,
                   s.status, s.operator_name, s.facilities, s.create_time, s.update_time,
                   COUNT(d.id) as total_devices,
                   SUM(CASE WHEN d.status = 'AVAILABLE' THEN 1 ELSE 0 END) as available_devices
            FROM charging_station s
            LEFT JOIN charging_device d ON s.id = d.station_id
            """;

    /**
     * 全量同步充电站数据
     * 每5分钟执行一次
     */
    @Scheduled(fixedDelayString = "${sync.sync-interval:300000}")
    @Transactional(readOnly = true)
    public void syncAllStations() {
        log.info("开始全量同步充电站数据");
        long startTime = System.currentTimeMillis();

        try {
            String sql = BASE_SQL + "GROUP BY s.id ORDER BY s.id";
            List<ChargingStationDocument> documents = jdbcTemplate.query(sql, new StationRowMapper());

            if (!documents.isEmpty()) {
                stationRepository.saveAll(documents);
                long elapsed = System.currentTimeMillis() - startTime;
                log.info("充电站数据同步完成，共同步 {} 条记录，耗时 {} ms", documents.size(), elapsed);
            } else {
                log.warn("没有充电站数据需要同步");
            }

        } catch (Exception e) {
            log.error("充电站数据同步失败", e);
            throw new RuntimeException("充电站数据同步失败", e);
        }
    }

    /**
     * 增量同步单个充电站
     * 用于实时同步单个充电站的变更
     *
     * @param stationId 充电站ID
     * @return 是否同步成功
     */
    public boolean syncStation(Long stationId) {
        if (stationId == null) {
            log.warn("充电站ID不能为空");
            return false;
        }

        log.info("增量同步充电站: {}", stationId);

        try {
            String sql = BASE_SQL + "WHERE s.id = ? GROUP BY s.id";

            Optional<ChargingStationDocument> docOpt = jdbcTemplate.query(
                    sql,
                    new StationRowMapper(),
                    stationId).stream().findFirst();

            if (docOpt.isPresent()) {
                stationRepository.save(docOpt.get());
                log.info("充电站增量同步完成: {}", stationId);
                return true;
            } else {
                log.warn("未找到充电站记录: {}", stationId);
                return false;
            }

        } catch (EmptyResultDataAccessException e) {
            log.warn("充电站不存在: {}", stationId);
            return false;
        } catch (Exception e) {
            log.error("充电站增量同步失败: {}", stationId, e);
            return false;
        }
    }

    /**
     * 删除ES中的充电站记录
     * 当MySQL中的充电站被删除时调用
     *
     * @param stationId 充电站ID
     */
    public void deleteStation(Long stationId) {
        if (stationId == null) {
            log.warn("充电站ID不能为空");
            return;
        }

        try {
            stationRepository.deleteById(stationId);
            log.info("已删除ES中的充电站记录: {}", stationId);
        } catch (Exception e) {
            log.error("删除ES中的充电站记录失败: {}", stationId, e);
        }
    }

    /**
     * 批量同步充电站（按ID列表）
     * 用于批量更新指定的充电站
     *
     * @param stationIds 充电站ID列表
     * @return 同步成功的数量
     */
    @Transactional(readOnly = true)
    public int syncStations(List<Long> stationIds) {
        if (stationIds == null || stationIds.isEmpty()) {
            log.warn("充电站ID列表为空");
            return 0;
        }

        log.info("批量同步充电站，数量: {}", stationIds.size());
        int successCount = 0;

        try {
            String inClause = String.join(",", stationIds.stream().map(String::valueOf).toList());
            String sql = BASE_SQL + "WHERE s.id IN (" + inClause + ") GROUP BY s.id";

            List<ChargingStationDocument> documents = jdbcTemplate.query(sql, new StationRowMapper());

            if (!documents.isEmpty()) {
                stationRepository.saveAll(documents);
                successCount = documents.size();
                log.info("批量同步充电站完成，成功 {} 条", successCount);
            }

        } catch (Exception e) {
            log.error("批量同步充电站失败", e);
        }

        return successCount;
    }

    /**
     * RowMapper实现，将ResultSet映射为ChargingStationDocument
     */
    private static class StationRowMapper implements RowMapper<ChargingStationDocument> {
        @Override
        public ChargingStationDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChargingStationDocument doc = new ChargingStationDocument();

            doc.setId(rs.getLong("id"));
            doc.setStationName(rs.getString("station_name"));
            doc.setAddress(rs.getString("address"));

            BigDecimal latitude = rs.getBigDecimal("latitude");
            if (latitude != null) {
                doc.setLatitude(latitude);
            }

            BigDecimal longitude = rs.getBigDecimal("longitude");
            if (longitude != null) {
                doc.setLongitude(longitude);
            }

            doc.setTotalDevices(rs.getInt("total_devices"));

            int availableDevices = rs.getInt("available_devices");
            if (!rs.wasNull()) {
                doc.setAvailableDevices(availableDevices);
            } else {
                doc.setAvailableDevices(0);
            }

            doc.setStatus(rs.getString("status"));
            doc.setOperatorName(rs.getString("operator_name"));
            doc.setFacilities(rs.getString("facilities"));

            LocalDateTime createTime = rs.getObject("create_time", LocalDateTime.class);
            if (createTime != null) {
                doc.setCreateTime(createTime);
            }

            LocalDateTime updateTime = rs.getObject("update_time", LocalDateTime.class);
            if (updateTime != null) {
                doc.setUpdateTime(updateTime);
            }

            return doc;
        }
    }
}
