package com.example.online_testing;

import com.example.online_testing.ErrorHandling.RestTemplateResponseErrorHandler;
import com.example.online_testing.RabbitMQ.BindingInterfaceOutput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableBinding(BindingInterfaceOutput.class)
@SpringBootApplication
@EnableEurekaClient
public class OnlineTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineTestingApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        return restTemplate;
    }

}
