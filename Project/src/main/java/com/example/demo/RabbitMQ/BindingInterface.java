package com.example.demo.RabbitMQ;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface BindingInterface {
    @Output("messagingChannel")
    MessageChannel greeting();
}