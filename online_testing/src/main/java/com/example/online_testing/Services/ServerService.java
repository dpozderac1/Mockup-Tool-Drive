package com.example.online_testing.Services;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.Server;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServerService {
    public List<Server> getAllServers();
    public ResponseEntity getServerByID(Long id);
    public ResponseEntity deleteServerByID(Long id);
    public List<Server> getUserServers(Long id);
    public ResponseEntity saveServer(Server server);
    public ResponseEntity updateServer(Server server, Long id);
}
