package com.novax.algorithm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AlgorithmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgorithmServiceApplication.class, args);
    }
}
