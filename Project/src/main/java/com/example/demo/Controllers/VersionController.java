package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class VersionController {

    private VersionService versionService;

    @Autowired
    public VersionController(VersionService versionService){
        this.versionService = versionService;
    }

    @PutMapping("/addOrUpdateVersion/{id}")
    ResponseEntity<?> addOrReplace(@RequestBody Version newVersion, @PathVariable Long id) {
        Version version = versionService.addOrReplace(newVersion, id);
        return new ResponseEntity<>(version, HttpStatus.OK);
    }

    @PutMapping("/changeVersion/{id}")
    ResponseEntity<?> changeVersion(@RequestBody VersionNames name, @PathVariable Long id) {
        Version version = versionService.changeVersion(name, id);
        return new ResponseEntity<>(version, HttpStatus.OK);
    }

    @GetMapping("/versions/project/{id}")
    ResponseEntity<?> allVersionsOfProject(@PathVariable Long id){
        List<Version> versions = versionService.allVersionsOfProject(id);
        return new ResponseEntity<>(versions, HttpStatus.OK);
    }

    @DeleteMapping("/delete/version/{id}")
    ResponseEntity<?> deleteOne(@PathVariable Long id){
        versionService.deleteOne(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addVersion")
    ResponseEntity<?> newVersion(@RequestBody Version newVersion) {
        Version version = versionService.newVersion(newVersion);
        return new ResponseEntity<>(version, HttpStatus.CREATED);
    }

    @GetMapping("/versions")
    ResponseEntity<?> getAllVersions(){
        List<Version> versions = versionService.getAllVersions();
        return new ResponseEntity<>(versions, HttpStatus.OK);
    }

    @GetMapping("/version/{id}")
    ResponseEntity<?> getOneVersion(@PathVariable Long id) {
        Version version = versionService.getOneVersion(id);
        return new ResponseEntity<>(version, HttpStatus.OK);
    }
}
