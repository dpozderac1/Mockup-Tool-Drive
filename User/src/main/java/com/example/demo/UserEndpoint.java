package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserEndpoint {
    GRPCUserService grpcUserService;
    @Autowired
    public UserEndpoint(GRPCUserService grpcUserService) {
        this.grpcUserService = grpcUserService;
    }
}
