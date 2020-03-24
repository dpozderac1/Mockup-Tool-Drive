package com.example.demo.Controllers;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.VersionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class MockupController {
    private MockupRepository mockupRepository;
    private VersionRepository versionRepository;

    public MockupController(MockupRepository mockupRepository, VersionRepository versionRepository) {
        this.mockupRepository = mockupRepository;
        this.versionRepository = versionRepository;
    }

    @PutMapping("/addOrUpdateMockup/{id}")
    ResponseEntity<Mockup>  addOrReplace(@RequestBody Mockup newMockup, @PathVariable Long id) {
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null) {
            if(!newMockup.getName().equals("")) mockup.setName(newMockup.getName());
            if(newMockup.getVersionId() != null) mockup.setVersionId(newMockup.getVersionId());
            if(newMockup.getDate_created() != null) mockup.setDate_created(newMockup.getDate_created());
            if(newMockup.getDate_modified() != null) mockup.setDate_modified(newMockup.getDate_modified());
            if(newMockup.getFile() != null) mockup.setFile(newMockup.getFile());
            if(newMockup.getAccessed_date() != null) mockup.setAccessed_date(newMockup.getAccessed_date());

            mockupRepository.save(mockup);
        }
        else{
            newMockup.setID(id);
            mockupRepository.save(newMockup);
        };
        return new ResponseEntity<>(mockup, HttpStatus.OK);
    }

    @GetMapping("/mockups/version/{id}")
    List<Mockup> allMockups(@PathVariable Long id){
        Version version = versionRepository.findByID(id);
        return mockupRepository.findAllByversionId(version);
    }

    @DeleteMapping("/delete/mockup/{id}")
    ResponseEntity<HttpStatus>  deleteOne(@PathVariable Long id){
        mockupRepository.deleteById(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addMockup")
    ResponseEntity<?> newMockup(@RequestBody Mockup newMockup) {
        Mockup mockup = mockupRepository.save(newMockup);
        return new ResponseEntity<>(mockup, HttpStatus.CREATED);
    }

    @GetMapping("/mockups")
    List<Mockup> all(){
        return mockupRepository.findAll();
    }

    @GetMapping("/mockup/{id}")
    Mockup one(@PathVariable Long id) {
        return mockupRepository.findByID(id);
    }

}
