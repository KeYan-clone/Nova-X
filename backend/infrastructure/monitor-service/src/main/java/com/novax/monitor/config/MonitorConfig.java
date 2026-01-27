package com.novax.monitor.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 监控服务配置
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@Configuration
public class MonitorConfig {

    /**
     * 负载均衡的 RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
