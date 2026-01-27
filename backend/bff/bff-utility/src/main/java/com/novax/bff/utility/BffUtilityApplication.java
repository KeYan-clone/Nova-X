package com.novax.bff.utility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 电力供应商 BFF 服务
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BffUtilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffUtilityApplication.class, args);
        System.out.println("""

                ========================================
                电力供应商BFF服务启动成功！
                服务端口: http://localhost:8204
                API文档: http://localhost:8204/doc.html
                ========================================
                """);
    }
}
