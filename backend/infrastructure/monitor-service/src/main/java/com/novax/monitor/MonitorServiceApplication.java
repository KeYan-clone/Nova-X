package com.novax.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 监控服务启动类
 * 统一可观测性平台 - 指标监控、日志聚合、链路追踪
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MonitorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorServiceApplication.class, args);
        System.out.println("""

            ========================================
            监控服务启动成功！
            服务端口: http://localhost:8104
            健康检查: http://localhost:8104/actuator/health
            Prometheus指标: http://localhost:8104/actuator/prometheus
            API文档: http://localhost:8104/doc.html
            ========================================
            """);
    }
}
