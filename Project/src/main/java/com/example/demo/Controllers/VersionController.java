package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.VersionService;
import org.json.JSONException;
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
        return versionService.addOrReplace(newVersion, id);
    }

    @PutMapping("/changeVersion/{id}")
    ResponseEntity<?> changeVersion(@RequestBody VersionNames name, @PathVariable Long id) {
        return versionService.changeVersion(name, id);
    }

    @GetMapping("/versions/project/{id}")
    ResponseEntity<?> allVersionsOfProject(@PathVariable Long id){
        return versionService.allVersionsOfProject(id);
    }

    @DeleteMapping("/delete/version/{id}")
    ResponseEntity<?> deleteOne(@PathVariable Long id) throws JSONException{
        return versionService.deleteOne(id);
    }

    @PostMapping("/addVersion")
    ResponseEntity<?> newVersion(@RequestBody Version newVersion) {
        return versionService.newVersion(newVersion);
    }

    @GetMapping("/versions")
    ResponseEntity<?> getAllVersions(){
        return versionService.getAllVersions();
    }

    @GetMapping("/version/{id}")
    ResponseEntity<?> getOneVersion(@PathVariable Long id) {
        return versionService.getOneVersion(id);
    }
}
