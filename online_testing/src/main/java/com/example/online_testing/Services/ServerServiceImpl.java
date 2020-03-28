package com.example.online_testing.Services;

import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.RoleRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    @Override
    public ResponseEntity getServerByID(Long id) {
        JSONObject jo = new JSONObject();
        if(serverRepository.existsByID(id)) {
            Server server = serverRepository.findByID(id);
            return new ResponseEntity(server, HttpStatus.OK);
        }
        else {
            jo.put("message", "Server does not exist!");
            return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity deleteServerByID(Long id) {
        JSONObject jo = new JSONObject();
        if(serverRepository.existsByID(id)) {
            serverRepository.deleteById(id);
            jo.put("message", "Server is successfully deleted!");
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        }
        jo.put("message", "Server does not exist!");
        return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Server> getUserServers(Long id) {
        User user = userRepository.findByID(id);
        List<Server> servers = serverRepository.findAllByuserID(user);
        return servers;
    }

    @Override
    public ResponseEntity saveServer(Server server) {
        Role admin = roleRepository.findByroleName(RoleNames.ADMIN);
        User user = userRepository.findByroleID(admin);
        server.setUserID(user);
        JSONObject jo = new JSONObject();
        List<Server> servers = serverRepository.findAll();
        boolean postoji = false;
        for (Server s: servers) {
            if(s.getUrl().equals(server.getUrl()) && s.getPort() == server.getPort() && s.getStatus().equals(server.getStatus()))  {
                postoji = true;
            }
        }
        if(!postoji) serverRepository.save(server);
        else {
            jo.put("message", "Server already exists!");
            return new ResponseEntity(jo.toString(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(server, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity updateServer(Server server, Long id) {
        Server oldServer = serverRepository.findByID(id);
        JSONObject jo = new JSONObject();
        if(oldServer == null) {
            jo.put("message", "The server you want to update does not exist!");
            return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
        }
        else{
            Server newServer = new Server(oldServer.getUrl(), oldServer.getPort(), oldServer.getStatus(), oldServer.getUserID());
            if(!server.getUrl().isEmpty()) {
                newServer.setUrl(server.getUrl());
            }
            if(!Integer.toString(server.getPort()).equals(Integer.toString(0))) {
                newServer.setPort(server.getPort());
            }
            if(!server.getStatus().isEmpty()) {
                newServer.setStatus(server.getStatus());
            }
            List<Server> servers = serverRepository.findAll();
            boolean postoji = false;
            for (Server s: servers) {
                if(s.getUrl().equals(newServer.getUrl()) && s.getPort() == newServer.getPort() && s.getStatus().equals(newServer.getStatus()))  {
                    postoji = true;
                }
            }
            if(!postoji) {
                oldServer.setUrl(newServer.getUrl());
                oldServer.setPort(newServer.getPort());
                oldServer.setStatus(newServer.getStatus());
                serverRepository.save(oldServer);
            }
            else {
                jo.put("message", "Server already exists!");
                return new ResponseEntity(jo.toString(), HttpStatus.CONFLICT);
            }

        }
        return new ResponseEntity(oldServer, HttpStatus.OK);
    }
}
