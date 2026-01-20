package com.novax.bff.oem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OEM BFF 服务
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BffOemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffOemApplication.class, args);
    }
}
