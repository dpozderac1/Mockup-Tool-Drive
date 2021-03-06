package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Services.ProjectService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PutMapping("/addOrUpdateProject/{id}")
    public ResponseEntity<?> addOrReplace(@Valid @RequestBody Project newProject, @PathVariable Long id) {
        return projectService.addOrReplace(newProject, id);
    }

    @PutMapping("/renameProject/{id}")
    public ResponseEntity<?> renameProject(@Valid @RequestBody String name, @PathVariable Long id) {
        return projectService.renameProject(name, id);
    }

    @DeleteMapping("/delete/project/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) throws JSONException {
        ResponseEntity<?> responseEntity = projectService.deleteOne(id);
        return responseEntity;
    }

    @PostMapping("/addProject/{id}")
    public ResponseEntity<Project> newProject(@Valid @RequestBody Project newProject, @PathVariable Long id) throws URISyntaxException {
        ResponseEntity<Project> projectResponseEntity = projectService.newProject(newProject, id);
        return projectResponseEntity;
    }

    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<?> getOneProject(@PathVariable Long id) {
        return projectService.getOneProject(id);
    }

    @GetMapping(value = "/filterFiles/{filter}/{id}")
    public ResponseEntity filterFiles(@PathVariable String filter, @PathVariable Long id) throws JSONException {
        return projectService.getFilesByFilter(filter, id);
    }

    @GetMapping(value="/searchFilesByName/{name}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity searchFilesByName(@PathVariable String name, @PathVariable Long id){
        return projectService.searchFilesByName(name, id);
    }

    @GetMapping(value = "/allFiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getAllFiles(@PathVariable Long id) {
        return projectService.getAllUserFiles(id);
    }

    @GetMapping(value = "/recentFiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getRecentFiles(@PathVariable Long id) {
        return projectService.getRecentUserFiles(id);
    }

}
