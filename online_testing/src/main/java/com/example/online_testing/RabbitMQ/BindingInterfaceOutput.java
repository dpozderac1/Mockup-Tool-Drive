package com.example.online_testing.RabbitMQ;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface BindingInterfaceOutput {
    @Output("receivingChannel")
    MessageChannel greeting();
}
