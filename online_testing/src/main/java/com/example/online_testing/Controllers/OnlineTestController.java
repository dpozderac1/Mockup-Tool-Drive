package com.example.online_testing.Controllers;

import com.example.online_testing.Models.*;
import com.example.online_testing.Services.OnlineTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@CrossOrigin
@RestController
public class OnlineTestController {

    @Autowired
    private OnlineTestService onlineTestService;

    @GetMapping("/onlineTests")
    public List<OnlineTest> all(){
        return onlineTestService.getAllOnlineTests();
    }

    @GetMapping("/onlineTest/{id}")
    public ResponseEntity oneId(@PathVariable Long id) {
        return onlineTestService.getOnlineTestByID(id);
    }

    @DeleteMapping(value = "/onlineTest/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteOnlineTest(@PathVariable Long id) {
        return onlineTestService.deleteOnlineTestByID(id);
    }

    @GetMapping("/onlineTestsServer/{id}")
    public List<OnlineTest> onlineTestsServers(@PathVariable Long id) {
        return onlineTestService.getOnlineTestsServers(id);
    }

    @GetMapping("/onlineTestsUser/{id}")
    public List<OnlineTest> onlineTestsUsers(@PathVariable Long id) {
        return onlineTestService.getOnlineTestsUsers(id);
    }

    @GetMapping("/onlineTestsGSPECDocument/{id}")
    public ResponseEntity onlineTestGSPECDocument(@PathVariable Long id) {
        return onlineTestService.getOnlineTestGSPECDocument(id);
    }

    @PostMapping("/addOnlineTest")
    public ResponseEntity addOnlineTest(@Valid @RequestBody OnlineTest onlineTest) {
        return onlineTestService.saveOnlineTest(onlineTest);
    }

    @PutMapping("/updateOnlineTest/{id}")
    public ResponseEntity updateOnlineTest(@Valid @RequestBody OnlineTest onlineTest, @PathVariable Long id) {
        return onlineTestService.updateOnlineTest(onlineTest, id);
    }
}
