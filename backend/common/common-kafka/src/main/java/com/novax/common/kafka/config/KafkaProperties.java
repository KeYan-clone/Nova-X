package com.novax.common.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Kafka 配置属性（与 Nacos common.yaml 对齐）
 */
@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    /**
     * Kafka 地址列表
     */
    private String bootstrapServers = "localhost:9092";

    private Consumer consumer = new Consumer();

    private Producer producer = new Producer();

    @Data
    public static class Consumer {
        private String groupId;
        private String autoOffsetReset;
        private Boolean enableAutoCommit = true;
        private Integer autoCommitInterval = 1000;
    }

    @Data
    public static class Producer {
        private String acks = "all";
        private Integer retries = 3;
    }
}
