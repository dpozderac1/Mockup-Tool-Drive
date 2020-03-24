package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class VersionController {
    private VersionRepository versionRepository;
    private ProjectRepository projectRepository;

    public VersionController(VersionRepository versionRepository, ProjectRepository projectRepository) {
        this.versionRepository = versionRepository;
        this.projectRepository = projectRepository;
    }

    @PutMapping("/addOrUpdateVersion/{id}")
    ResponseEntity<Version> addOrReplace(@RequestBody Version newVersion, @PathVariable Long id) {
        Version version = versionRepository.findByID(id);
            if(version != null){
                if(!newVersion.getVersion_name().equals("")) version.setVersion_name(newVersion.getVersion_name());
                if(newVersion.getProjectId() != null) version.setProjectId(newVersion.getProjectId());
                versionRepository.save(version);
            }
            else {
                newVersion.setID(id);
                versionRepository.save(newVersion);
            };
        return new ResponseEntity<>(version, HttpStatus.OK);
    }

    @PutMapping("/changeVersion/{id}")
    ResponseEntity<Version> changeVersion(@RequestBody VersionNames name, @PathVariable Long id) {
        Version version = versionRepository.findByID(id);
        if(!name.equals("")){
            version.setVersion_name(name);
            versionRepository.save(version);
        }
        return new ResponseEntity<>(version, HttpStatus.OK);
    }

    @GetMapping("/versions/project/{id}")
    List<Version> allVersions(@PathVariable Long id){
        Project project = projectRepository.findByID(id);
        return versionRepository.findAllByprojectId(project);
    }

    @DeleteMapping("/delete/version/{id}")
    ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id){
        versionRepository.deleteById(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addVersion")
    ResponseEntity<?> newVersion(@RequestBody Version newVersion) {
        Version version = versionRepository.save(newVersion);
        return new ResponseEntity<>(version, HttpStatus.CREATED);
    }

    @GetMapping("/versions")
    List<Version> all(){
        return versionRepository.findAll();
    }

    @GetMapping("/version/{id}")
    Version one(@PathVariable Long id) {
        return versionRepository.findByID(id);
    }
}
