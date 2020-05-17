package com.example.online_testing.Services;

import com.example.online_testing.ErrorHandling.AlreadyExistsException;
import com.example.online_testing.ErrorHandling.RecordNotFoundException;
import com.example.online_testing.GRPCOnlineTestingService;
import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.RoleRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    GRPCOnlineTestingService grpcOnlineTestingService;

    @Override
    public List<Server> getAllServers() {
        grpcOnlineTestingService.action("online-testing","GET","/servers","SUCESS", new Timestamp(System.currentTimeMillis()));
        return serverRepository.findAll();
    }

    @Override
    public ResponseEntity getServerByID(Long id) {
        if(serverRepository.existsByID(id)) {
            grpcOnlineTestingService.action("online-testing","GET","/server/{id}","SUCESS", new Timestamp(System.currentTimeMillis()));
            Server server = serverRepository.findByID(id);
            return new ResponseEntity(server, HttpStatus.OK);
        }
        else {
            grpcOnlineTestingService.action("online-testing","GET","/server/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("Server does not exist!");
        }
    }

    @Override
    public ResponseEntity deleteServerByID(Long id) {
        JSONObject jo = new JSONObject();
        if(serverRepository.existsByID(id)) {
            grpcOnlineTestingService.action("online-testing","DELETE","/server/{id}","SUCESS", new Timestamp(System.currentTimeMillis()));
            serverRepository.deleteById(id);
            jo.put("message", "Server is successfully deleted!");
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        }
        else{
            grpcOnlineTestingService.action("online-testing","DELETE","/server/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("Server does not exist!");
        }

    }

    @Override
    public List<Server> getUserServers(Long id) {
        grpcOnlineTestingService.action("online-testing","GET","/userServers/{id}","SUCESS", new Timestamp(System.currentTimeMillis()));
        User user = userRepository.findByID(id);
        List<Server> servers = serverRepository.findAllByuserID(user);
        return servers;
    }

    @Override
    public ResponseEntity saveServer(Server server) {
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
        if(!postoji) {
            grpcOnlineTestingService.action("online-testing","POST","/addServer","SUCESS", new Timestamp(System.currentTimeMillis()));
            serverRepository.save(server);
        }
        else {
            grpcOnlineTestingService.action("online-testing","POST","/addServer","CONFLICT", new Timestamp(System.currentTimeMillis()));
            throw new AlreadyExistsException("Server already exists!");
        }
        return new ResponseEntity(server, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity updateServer(Server server, Long id) {
        Server oldServer = serverRepository.findByID(id);
        if(oldServer == null) {
            grpcOnlineTestingService.action("online-testing","PUT","/updateServer/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("The server you want to update does not exist!");
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
                grpcOnlineTestingService.action("online-testing","PUT","/updateServer/{id}","SUCESS", new Timestamp(System.currentTimeMillis()));
                oldServer.setUrl(newServer.getUrl());
                oldServer.setPort(newServer.getPort());
                oldServer.setStatus(newServer.getStatus());
                serverRepository.save(oldServer);
            }
            else {
                grpcOnlineTestingService.action("online-testing","PUT","/updateServer/{id}","CONFLICT", new Timestamp(System.currentTimeMillis()));
                throw new AlreadyExistsException("Server already exists!");
            }

        }
        return new ResponseEntity(oldServer, HttpStatus.OK);
    }
}
