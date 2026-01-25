package com.novax.common.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka 主题配置（服务级）
 */
@Data
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicProperties {

    /**
     * 主题映射，例如：
     * kafka.topics.order-created=order.created
     */
    private Map<String, String> values = new HashMap<>();
}
