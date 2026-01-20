package com.novax.bff.operator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 运营商 BFF 服务
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BffOperatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffOperatorApplication.class, args);
    }
}
