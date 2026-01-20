package com.novax.bff.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 管理后台 BFF 服务
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BffAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffAdminApplication.class, args);
    }
}
