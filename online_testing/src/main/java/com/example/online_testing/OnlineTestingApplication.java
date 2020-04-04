package com.example.online_testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OnlineTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineTestingApplication.class, args);
    }

}
