package com.example.online_testing.Controllers;

import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.RoleRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
import com.example.online_testing.Services.BrowserService;
import com.example.online_testing.Services.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ServerController {

    @Autowired
    private ServerService serverService;

    @GetMapping("/servers")
    List<Server> all(){
        return serverService.getAllServers();
    }

    @GetMapping("/server/{id}")
    ResponseEntity oneId(@PathVariable Long id) {
        return serverService.getServerByID(id);
    }

    @DeleteMapping("/server/{id}")
    ResponseEntity deleteServer(@PathVariable Long id) {
        return serverService.deleteServerByID(id);
    }

    @GetMapping("/userServers/{id}")
    List<Server> userServers(@PathVariable Long id) {
        return serverService.getUserServers(id);
    }

    @PostMapping("/addServer")
    ResponseEntity addServer(@RequestBody Server server) {
        return serverService.saveServer(server);
    }

    @PutMapping("/updateServer/{id}")
    ResponseEntity updateServer(@RequestBody Server server, @PathVariable Long id) {
        return serverService.updateServer(server, id);
    }

}
