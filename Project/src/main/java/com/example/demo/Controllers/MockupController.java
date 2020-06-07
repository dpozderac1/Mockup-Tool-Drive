package com.example.demo.Controllers;

import com.example.demo.Models.Mockup;
import com.example.demo.Services.MockupService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;


@RestController
public class MockupController {

    private MockupService mockupService;

    @Autowired
    public MockupController(MockupService mockupService) {
        this.mockupService = mockupService;
    }

    @PutMapping("/addOrUpdateMockup/{id}")
    public ResponseEntity<?>  addOrReplace(@Valid @RequestBody Mockup newMockup, @PathVariable Long id) {
        return mockupService.addOrReplace(newMockup,id);
    }

    @GetMapping("/mockups/version/{id}")
    public ResponseEntity<?>  allMockupsOfVersion(@PathVariable Long id){
        return mockupService.allMockupsOfVersion(id);
    }

    @DeleteMapping("/delete/mockup/{id}")
    public ResponseEntity<?>  deleteOne(@PathVariable Long id) throws JSONException {
        return mockupService.deleteOne(id);
    }

    @PostMapping("/addMockup")
    public ResponseEntity<?> newMockup(@Valid @RequestBody Mockup newMockup) {
        return mockupService.newMockup(newMockup);
    }

    @GetMapping("/mockups")
    public ResponseEntity<?> getAllMockups(){
        return mockupService.getAllMockups();
    }

    @GetMapping("/mockup/{id}")
    public ResponseEntity<?>  getOneMockup(@PathVariable Long id) {
        return mockupService.getOneMockup(id);
    }

    @PutMapping(value = "/addOrUpdateFile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addOrUpdateFile(@PathVariable Long id,@RequestParam("file") MultipartFile fajl) throws IOException, SQLException {
        System.out.println("Usao u kontroler!!!!!!!");
        return mockupService.addOrUpdateFile(fajl,id);
    }

    @GetMapping("/mockup/file/{id}")
    public ResponseEntity<?> getOneFile(@PathVariable Long id) throws SQLException {
        return mockupService.getOneFile(id);
    }
}
