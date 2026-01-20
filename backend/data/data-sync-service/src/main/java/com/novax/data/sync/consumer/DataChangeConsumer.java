package com.novax.data.sync.consumer;

import com.novax.data.sync.service.SessionSyncService;
import com.novax.data.sync.service.StationSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 数据变更事件消费者
 * 监听业务服务发出的数据变更事件，触发增量同步
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataChangeConsumer {

    private final StationSyncService stationSyncService;
    private final SessionSyncService sessionSyncService;
    private final ObjectMapper objectMapper;

    /**
     * 监听充电站变更事件
     */
    @KafkaListener(topics = "station-events", groupId = "data-sync-group")
    public void handleStationChange(String message) {
        try {
            log.info("收到充电站变更事件: {}", message);

            JsonNode event = objectMapper.readTree(message);
            Long stationId = event.get("stationId").asLong();

            // 触发增量同步
            stationSyncService.syncStation(stationId);

        } catch (Exception e) {
            log.error("处理充电站变更事件失败: {}", message, e);
        }
    }

    /**
     * 监听充电会话变更事件
     */
    @KafkaListener(topics = "session-events", groupId = "data-sync-group")
    public void handleSessionChange(String message) {
        try {
            log.info("收到充电会话变更事件: {}", message);

            JsonNode event = objectMapper.readTree(message);
            Long sessionId = event.get("sessionId").asLong();

            // 触发增量同步
            sessionSyncService.syncSession(sessionId);

        } catch (Exception e) {
            log.error("处理充电会话变更事件失败: {}", message, e);
        }
    }
}
