package com.novax.drvpp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DrVppServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrVppServiceApplication.class, args);
    }
}
