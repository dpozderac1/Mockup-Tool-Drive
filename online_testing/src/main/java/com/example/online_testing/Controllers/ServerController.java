package com.example.online_testing.Controllers;

import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.RoleRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
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

    private ServerRepository serverRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public ServerController(ServerRepository serverRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/servers")
    List<Server> all(){
        return serverRepository.findAll();
    }

    @GetMapping("/server/{id}")
    ResponseEntity oneId(@PathVariable Long id) {
        if(serverRepository.existsByID(id)) {
            Server server = serverRepository.findByID(id);
            return new ResponseEntity(server, HttpStatus.OK);
        }
        else {
            return new ResponseEntity("Server does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/server/{id}")
    ResponseEntity deleteServer(@PathVariable Long id) {
        if(serverRepository.existsByID(id)) {
            serverRepository.deleteById(id);
            return new ResponseEntity("Server is successfully deleted!", HttpStatus.OK);
        }
        return new ResponseEntity("Server does not exist!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/userServers/{id}")
    List<Server> userServers(@PathVariable Long id) {
        User user = userRepository.findByID(id);
        List<Server> servers = serverRepository.findAllByuserID(user);
        return servers;
    }

    @PostMapping("/addServer")
    ResponseEntity addServer(@RequestBody Server server) {
        Role admin = roleRepository.findByroleName(RoleNames.ADMIN);
        User user = userRepository.findByroleID(admin);
        server.setUserID(user);
        List<Server> servers = serverRepository.findAll();
        boolean postoji = false;
        for (Server s: servers) {
            if(s.getUrl().equals(server.getUrl()) && s.getPort() == server.getPort() && s.getStatus().equals(server.getStatus()))  {
                postoji = true;
            }
        }
        if(!postoji) serverRepository.save(server);
        else return new ResponseEntity("Server already exists!", HttpStatus.CONFLICT);
        return new ResponseEntity(server, HttpStatus.OK);
    }

    @PutMapping("/updateServer/{id}")
    ResponseEntity updateServer(@RequestBody Server server, @PathVariable Long id) {
        Server oldServer = serverRepository.findByID(id);
        if(oldServer == null) {
            return new ResponseEntity("The server you want to update does not exist!", HttpStatus.NOT_FOUND);
        }
        else{
            if(!server.getUrl().isEmpty()) {
                oldServer.setUrl(server.getUrl());
            }
            if(!Integer.toString(server.getPort()).equals(Integer.toString(0))) {
                oldServer.setPort(server.getPort());
            }
            if(!server.getStatus().isEmpty()) {
                oldServer.setStatus(server.getStatus());
            }
            List<Server> servers = serverRepository.findAll();
            boolean postoji = false;
            for (Server s: servers) {
                if(s.getUrl().equals(oldServer.getUrl()) && s.getPort() == oldServer.getPort() && s.getStatus().equals(oldServer.getStatus()))  {
                    postoji = true;
                }
            }
            if(!postoji) serverRepository.save(oldServer);
            else return new ResponseEntity("Server already exists!", HttpStatus.CONFLICT);
        }
        return new ResponseEntity(oldServer, HttpStatus.OK);
    }




}
