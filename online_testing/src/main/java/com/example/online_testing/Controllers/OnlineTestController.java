package com.example.online_testing.Controllers;

import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.GSPECDocumentRepository;
import com.example.online_testing.Repositories.OnlineTestRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
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

    private OnlineTestRepository onlineTestRepository;
    private ServerRepository serverRepository;
    private UserRepository userRepository;
    private GSPECDocumentRepository gspecDocumentRepository;

    @Autowired
    public OnlineTestController(OnlineTestRepository onlineTestRepository, ServerRepository serverRepository, UserRepository userRepository, GSPECDocumentRepository gspecDocumentRepository) {
        this.onlineTestRepository = onlineTestRepository;
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
        this.gspecDocumentRepository = gspecDocumentRepository;
    }

    @GetMapping("/onlineTests")
    List<OnlineTest> all(){
        return onlineTestRepository.findAll();
    }

    @GetMapping("/onlineTest/{id}")
    ResponseEntity oneId(@PathVariable Long id) {
        if(onlineTestRepository.existsByID(id)) {
            OnlineTest onlineTest = onlineTestRepository.findByID(id);
            return new ResponseEntity(onlineTest, HttpStatus.OK);
        }
        else {
            return new ResponseEntity("Online test does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/onlineTest/{id}")
    ResponseEntity deleteOnlineTest(@PathVariable Long id) {
        if(onlineTestRepository.existsByID(id)) {
            onlineTestRepository.deleteById(id);
            return new ResponseEntity("Online test is successfully deleted!", HttpStatus.OK);
        }
        return new ResponseEntity("Online test does not exist!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/onlineTestsServer/{id}")
    List<OnlineTest> onlineTestsServers(@PathVariable Long id) {
        Server server = serverRepository.findByID(id);
        List<OnlineTest> onlineTests = onlineTestRepository.findAllByserverID(server);
        return onlineTests;
    }

    @GetMapping("/onlineTestsUsers/{id}")
    List<OnlineTest> onlineTestsUsers(@PathVariable Long id) {
        User user = userRepository.findByID(id);
        List<OnlineTest> onlineTests = onlineTestRepository.findAllByuserID(user);
        return onlineTests;
    }

    @GetMapping("/onlineTestGSPECDocument/{id}")
    ResponseEntity onlineTestGSPECDocument(@PathVariable Long id) {
        GSPECDocument gspecDocument = gspecDocumentRepository.findByID(id);
        OnlineTest onlineTest = onlineTestRepository.findBygspecDocumentID(gspecDocument);
        if(onlineTest == null) {
            return new ResponseEntity("Online test does not exist!", HttpStatus.NOT_FOUND);
        }
        else return new ResponseEntity(onlineTest, HttpStatus.OK);
    }

    @PostMapping("/addOnlineTest")
    ResponseEntity addOnlineTest(@RequestBody OnlineTest onlineTest) {
        Server server = serverRepository.findByID(Long.valueOf(onlineTest.getIdServer()));
        User user = userRepository.findByID(Long.valueOf(onlineTest.getIdUser()));
        GSPECDocument gspecDocument = gspecDocumentRepository.findByID(Long.valueOf(onlineTest.getIdGspecDocument()));
        if(server == null) {
            return new ResponseEntity("Server does not exist!", HttpStatus.NOT_FOUND);
        }
        else if(user == null) {
            return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);
        }
        else if(gspecDocument == null) {
            return new ResponseEntity("GSPEC Document does not exist!", HttpStatus.NOT_FOUND);
        }
        else {
            OnlineTest onlineTest11 = onlineTestRepository.findBygspecDocumentID(gspecDocument);
            if(onlineTest11 == null){
                OnlineTest onlineTest1 = new OnlineTest(onlineTest.getTests(), onlineTest.getTest_results(), server, user, gspecDocument);
                List<OnlineTest> onlineTests = onlineTestRepository.findAll();
                boolean postoji = false;
                for (OnlineTest ot: onlineTests) {
                    if(ot.getTests().equals(onlineTest1.getTests()) && ot.getTest_results().equals(onlineTest1.getTest_results()) && ot.getServerID().equals(onlineTest1.getServerID()) && ot.getUserID().equals(onlineTest1.getUserID()) && ot.getGspecDocumentID().equals(onlineTest1.getGspecDocumentID()))  {
                        postoji = true;
                    }
                }
                if(!postoji) onlineTestRepository.save(onlineTest1);
                else return new ResponseEntity("Online test already exists!", HttpStatus.CONFLICT);
            }
            else return new ResponseEntity("Online test for this GSPEC document already exists!", HttpStatus.OK);
        }
        return new ResponseEntity("Online test is successfully added!", HttpStatus.CREATED);
    }

    @PutMapping("/updateOnlineTest/{id}")
    ResponseEntity updateOnlineTest(@RequestBody OnlineTest onlineTest, @PathVariable Long id) {
        OnlineTest onlineTest1 = onlineTestRepository.findByID(id);
        if(onlineTest1 == null) {
            return new ResponseEntity("Online test does not exist!", HttpStatus.NOT_FOUND);
        }
        else {
            if(!onlineTest.getTests().isEmpty()) {
                onlineTest1.setTests(onlineTest.getTests());
            }
            if(onlineTest.getTest_results() != null) {
                onlineTest1.setTest_results(onlineTest.getTest_results());
            }
            if(!Integer.toString(onlineTest.getIdServer()).equals(Integer.toString(0))) {
                Server server = serverRepository.findByID(Long.valueOf(onlineTest.getIdServer()));
                if (server == null) {
                    return new ResponseEntity("Server does not exist!", HttpStatus.NOT_FOUND);
                }
                else {
                    onlineTest1.setServerID(server);
                }
            }
            if(!Integer.toString(onlineTest.getIdUser()).equals(Integer.toString(0))) {
                User user = userRepository.findByID(Long.valueOf(onlineTest.getIdUser()));
                if (user == null) {
                    return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);
                }
                else {
                    onlineTest1.setUserID(user);
                }
            }
            if(!Integer.toString(onlineTest.getIdGspecDocument()).equals(Integer.toString(0))) {
                GSPECDocument gspecDocument = gspecDocumentRepository.findByID(Long.valueOf(onlineTest.getIdGspecDocument()));
                if (gspecDocument == null) {
                    return new ResponseEntity("GSPEC Document does not exist!", HttpStatus.NOT_FOUND);
                }
                else {
                    onlineTest1.setGspecDocumentID(gspecDocument);
                }
            }
            /*List<OnlineTest> onlineTests = onlineTestRepository.findAll();
            boolean postoji = false;
            for (OnlineTest ot: onlineTests) {
                if(ot.getTests().equals(onlineTest1.getTests()) && ot.getTest_results().equals(onlineTest1.getTest_results()) && ot.getServerID().equals(onlineTest1.getServerID()) && ot.getUserID().equals(onlineTest1.getUserID()) && ot.getGspecDocumentID().equals(onlineTest1.getGspecDocumentID()))  {
                    postoji = true;
                }
            }
            if(!postoji) onlineTestRepository.save(onlineTest1);
            else return new ResponseEntity("Online test already exists!", HttpStatus.CONFLICT);*/
            onlineTestRepository.save(onlineTest1);
        }
        return new ResponseEntity(onlineTest1, HttpStatus.OK);
    }
}
