package com.example.demo.Controllers;

import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Services.VersionService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class VersionController {

    private VersionService versionService;

    @Autowired
    public VersionController(VersionService versionService){
        this.versionService = versionService;
    }

    @PutMapping("/addOrUpdateVersion/{id}")
    public ResponseEntity<?> addOrReplace(@Valid @RequestBody Version newVersion, @PathVariable Long id) {
        return versionService.addOrReplace(newVersion, id);
    }

    @PutMapping("/changeVersion/{id}")
    public ResponseEntity<?> changeVersion(@Valid @RequestBody VersionNames name, @PathVariable Long id) {
        return versionService.changeVersion(name, id);
    }

    @GetMapping("/versions/project/{id}")
    public ResponseEntity<?> allVersionsOfProject(@PathVariable Long id){
        return versionService.allVersionsOfProject(id);
    }

    @DeleteMapping("/delete/version/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) throws JSONException{
        return versionService.deleteOne(id);
    }

    @PostMapping("/addVersion")
    public ResponseEntity<?> newVersion(@RequestBody Version newVersion) {
        return versionService.newVersion(newVersion);
    }

    @GetMapping("/versions")
    public ResponseEntity<?> getAllVersions(){
        return versionService.getAllVersions();
    }

    @GetMapping("/version/{id}")
    public ResponseEntity<?> getOneVersion(@PathVariable Long id) {
        return versionService.getOneVersion(id);
    }
}
