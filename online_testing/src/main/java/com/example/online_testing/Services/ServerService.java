package com.example.online_testing.Services;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.Server;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServerService {
     List<Server> getAllServers();
     ResponseEntity getServerByID(Long id);
     ResponseEntity deleteServerByID(Long id);
     List<Server> getUserServers(Long id);
     ResponseEntity saveServer(Server server);
     ResponseEntity updateServer(Server server, Long id);
}
