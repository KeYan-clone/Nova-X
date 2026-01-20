package com.novax.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class IotGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotGatewayServiceApplication.class, args);
    }
}
