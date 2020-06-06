package com.example.online_testing.Controllers;

import com.example.online_testing.Models.*;
import com.example.online_testing.Services.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@CrossOrigin
@RestController
public class ServerController {

    @Autowired
    private ServerService serverService;

    @GetMapping("/servers")
    public List<Server> all(){
        return serverService.getAllServers();
    }

    @GetMapping("/server/{id}")
    public ResponseEntity oneId(@PathVariable Long id) {
        return serverService.getServerByID(id);
    }

    @DeleteMapping(value = "/server/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteServer(@PathVariable Long id) {
        return serverService.deleteServerByID(id);
    }

    @GetMapping("/userServers/{id}")
    public List<Server> userServers(@PathVariable Long id) {
        return serverService.getUserServers(id);
    }

    @PostMapping("/addServer")
    public ResponseEntity addServer(@Valid @RequestBody Server server) {
        return serverService.saveServer(server);
    }

    @PutMapping("/updateServer/{id}")
    public ResponseEntity updateServer(@Valid @RequestBody Server server, @PathVariable Long id) {
        return serverService.updateServer(server, id);
    }

}
