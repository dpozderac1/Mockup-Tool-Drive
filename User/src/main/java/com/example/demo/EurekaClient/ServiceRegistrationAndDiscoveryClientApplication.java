package com.example.demo.EurekaClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ServiceRegistrationAndDiscoveryClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistrationAndDiscoveryClientApplication.class, args);
    }
}