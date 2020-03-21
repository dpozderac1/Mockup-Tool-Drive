package com.example.online_testing.Controllers;

import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ServerController {

    private ServerRepository serverRepository;

    @Autowired
    public ServerController(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @GetMapping("/servers")
    List<Server> all(){
        return serverRepository.findAll();
    }

    @GetMapping("/server/{id}")
    Server oneId(@PathVariable Long id) {
        return serverRepository.findByID(id);
    }

    @DeleteMapping("/server/{id}")
    String deleteServer(@PathVariable Long id) {
        if(serverRepository.existsByID(id)) {
            serverRepository.deleteById(id);
            return "Server is successfully deleted!\n";
        }
        return "Server does not exist!\n";
    }

    @GetMapping("/userServers/{id}")
    List<Server> userServers(@PathVariable Long id) {
        List<Server> servers = serverRepository.findAll();
        ArrayList<Server> returnServers = new ArrayList<>();
        for (Server s: servers) {
            if(s.getUser_ID().getID().equals(id)) returnServers.add(s);
        }
        return returnServers;
    }

    @PostMapping("/addServer")
    String addServer(@RequestBody Server server) {
        if(!serverRepository.existsByurl(server.getUrl())) {
            if(server.getUser_ID().getRole_ID().equals(RoleNames.ADMIN)) {
                serverRepository.save(server);
            }
            return "OK!";
        }
        return "Not OK!";
    }

    @PutMapping("/updateServer/{id}")
    String updateServer(@RequestBody Server server, @PathVariable Long id) {
        Server oldServer = serverRepository.findByID(id);
        if(oldServer != null) {
            oldServer.setUrl(server.getUrl());
            oldServer.setPort(server.getPort());
            oldServer.setStatus(server.getStatus());
            serverRepository.save(oldServer);
            return "OK!";
        }
        return "Not OK!";
    }




}
