package com.novax.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MonitorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorServiceApplication.class, args);
    }
}
