package com.example.online_testing.RabbitMQ;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface BindingInterface {

    String GREETING = "messagingChannel";

    @Input(GREETING)
    SubscribableChannel greeting();
}