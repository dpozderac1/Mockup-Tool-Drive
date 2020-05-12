package com.example.demo.RabbitMQ;

import com.example.demo.RabbitMQ.Command;

public class MessageRabbitMq {
    private Long id;
    Command command;

    public MessageRabbitMq(Long id, Command command) {
        this.id = id;
        this.command = command;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
