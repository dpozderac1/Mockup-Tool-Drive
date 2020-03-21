package com.example.online_testing.Controllers;

import com.example.online_testing.Models.OnlineTest;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Repositories.OnlineTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class OnlineTestController {

    private OnlineTestRepository onlineTestRepository;

    @Autowired
    public OnlineTestController(OnlineTestRepository onlineTestRepository) {
        this.onlineTestRepository = onlineTestRepository;
    }

    @GetMapping("/onlineTests")
    List<OnlineTest> all(){
        return onlineTestRepository.findAll();
    }

    @GetMapping("/onlineTest/{id}")
    OnlineTest oneId(@PathVariable Long id) {
        return onlineTestRepository.findByID(id);
    }

    @DeleteMapping("/onlineTest/{id}")
    String deleteOnlineTest(@PathVariable Long id) {
        if(onlineTestRepository.existsByID(id)) {
            onlineTestRepository.deleteById(id);
            return "Online test is successfully deleted!\n";
        }
        return "Online test does not exist!\n";
    }

    @GetMapping("/onlineTestsServer/{id}")
    List<OnlineTest> onlineTestsServers(@PathVariable Long id) {
        List<OnlineTest> onlineTests = onlineTestRepository.findAll();
        ArrayList<OnlineTest> returnOnlineTests = new ArrayList<>();
        for (OnlineTest ot: onlineTests) {
            if(ot.getServer_ID().getID().equals(id)) returnOnlineTests.add(ot);
        }
        return returnOnlineTests;
    }

    @GetMapping("/onlineTestsUsers/{id}")
    List<OnlineTest> onlineTestsUsers(@PathVariable Long id) {
        List<OnlineTest> onlineTests = onlineTestRepository.findAll();
        ArrayList<OnlineTest> returnOnlineTests = new ArrayList<>();
        for (OnlineTest ot: onlineTests) {
            if(ot.getUser_ID().getID().equals(id)) returnOnlineTests.add(ot);
        }
        return returnOnlineTests;
    }

    @GetMapping("/onlineTestGSPECDocument/{id}")
    OnlineTest onlineTestGSPECDocument(@PathVariable Long id) {
        List<OnlineTest> onlineTests = onlineTestRepository.findAll();
        for (OnlineTest ot: onlineTests) {
            if(ot.getGspec_document_ID().getID().equals(id)) return ot;
        }
        return null;
    }

    @PostMapping("/addOnlineTest")
    String addOnlineTest(@RequestBody OnlineTest onlineTest) {
        onlineTestRepository.save(onlineTest);
        return "OK!";
    }

    @PutMapping("/updateOnlineTest/{id}")
    String updateOnlineTest(@RequestBody OnlineTest onlineTest, @PathVariable Long id) {
        OnlineTest oldOnlineTest = onlineTestRepository.findByID(id);
        if(oldOnlineTest != null) {
            oldOnlineTest.setTests(onlineTest.getTests());
            oldOnlineTest.setTest_results(onlineTest.getTest_results());
            oldOnlineTest.setServer_ID(onlineTest.getServer_ID());
            oldOnlineTest.setUser_ID(onlineTest.getUser_ID());
            onlineTestRepository.save(oldOnlineTest);
            return "OK!";
        }
        return "Not OK!";
    }
}
