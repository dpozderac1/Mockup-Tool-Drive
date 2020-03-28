package com.example.demo.Controllers;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.MockupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class MockupController {

    private MockupService mockupService;

    @Autowired
    public MockupController(MockupService mockupService) {
        this.mockupService = mockupService;
    }

    @PutMapping("/addOrUpdateMockup/{id}")
    ResponseEntity<?>  addOrReplace(@RequestBody Mockup newMockup, @PathVariable Long id) {
        Mockup mockup = mockupService.addOrReplace(newMockup,id);
        return new ResponseEntity<>(mockup, HttpStatus.OK);
    }

    @GetMapping("/mockups/version/{id}")
    ResponseEntity<?>  allMockupsOfVersion(@PathVariable Long id){
        List<Mockup> mockups = mockupService.allMockupsOfVersion(id);
        return new ResponseEntity<>(mockups, HttpStatus.OK);
    }

    @DeleteMapping("/delete/mockup/{id}")
    ResponseEntity<?>  deleteOne(@PathVariable Long id){
        mockupService.deleteOne(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addMockup")
    ResponseEntity<?> newMockup(@RequestBody Mockup newMockup) {
        Mockup mockup = mockupService.newMockup(newMockup);
        return new ResponseEntity<>(mockup, HttpStatus.CREATED);
    }

    @GetMapping("/mockups")
    ResponseEntity<?> getAllMockups(){
        List<Mockup> mockups = mockupService.getAllMockups();
        return new ResponseEntity<>(mockups, HttpStatus.OK);
    }

    @GetMapping("/mockup/{id}")
    ResponseEntity<?>  getOneMockup(@PathVariable Long id) {
        Mockup mockup = mockupService.getOneMockup(id);
        return new ResponseEntity<>(mockup, HttpStatus.OK);
    }

}
