package com.example.demo.RabbitMQ;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface BindingInterfaceInput {
    String GREETING = "receivingChannel";

    @Input(GREETING)
    SubscribableChannel greeting();
}
