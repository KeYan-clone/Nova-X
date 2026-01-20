package com.novax.bff.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * C端用户 BFF 服务
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BffConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffConsumerApplication.class, args);
    }
}
