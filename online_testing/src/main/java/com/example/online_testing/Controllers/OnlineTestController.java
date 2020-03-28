package com.example.online_testing.Controllers;

import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.GSPECDocumentRepository;
import com.example.online_testing.Repositories.OnlineTestRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
import com.example.online_testing.Services.OnlineTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class OnlineTestController {

    @Autowired
    private OnlineTestService onlineTestService;

    @GetMapping("/onlineTests")
    List<OnlineTest> all(){
        return onlineTestService.getAllOnlineTests();
    }

    @GetMapping("/onlineTest/{id}")
    ResponseEntity oneId(@PathVariable Long id) {
        return onlineTestService.getOnlineTestByID(id);
    }

    @DeleteMapping("/onlineTest/{id}")
    ResponseEntity deleteOnlineTest(@PathVariable Long id) {
        return onlineTestService.deleteOnlineTestByID(id);
    }

    @GetMapping("/onlineTestsServer/{id}")
    List<OnlineTest> onlineTestsServers(@PathVariable Long id) {
        return onlineTestService.getOnlineTestsServers(id);
    }

    @GetMapping("/onlineTestsUser/{id}")
    List<OnlineTest> onlineTestsUsers(@PathVariable Long id) {
        return onlineTestService.getOnlineTestsUsers(id);
    }

    @GetMapping("/onlineTestsGSPECDocument/{id}")
    ResponseEntity onlineTestGSPECDocument(@PathVariable Long id) {
        return onlineTestService.getOnlineTestGSPECDocument(id);
    }

    @PostMapping("/addOnlineTest")
    ResponseEntity addOnlineTest(@RequestBody OnlineTest onlineTest) {
        return onlineTestService.saveOnlineTest(onlineTest);
    }

    @PutMapping("/updateOnlineTest/{id}")
    ResponseEntity updateOnlineTest(@RequestBody OnlineTest onlineTest, @PathVariable Long id) {
        return onlineTestService.updateOnlineTest(onlineTest, id);
    }
}
