package com.novax.bff.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 管理后台 BFF 服务
 *
 * @author Nova-X Team
 * @since 2026-01-25
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BffAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffAdminApplication.class, args);
        System.out.println("""

                ========================================
                管理后台BFF服务启动成功！
                服务端口: http://localhost:8205
                API文档: http://localhost:8205/doc.html
                ========================================
                """);
    }
}
