package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectEndpoint {
    GRPCProjectService grpcProjectService;

    public ProjectEndpoint(GRPCProjectService grpcProjectService) {
        this.grpcProjectService = grpcProjectService;
    }
}
