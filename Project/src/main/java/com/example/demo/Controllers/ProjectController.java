package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Services.ProjectService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
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
    ResponseEntity<?> addOrReplace(@Valid @RequestBody Project newProject, @PathVariable Long id) {
        return projectService.addOrReplace(newProject, id);
    }

    @PutMapping("/renameProject/{id}")
    ResponseEntity<?> renameProject(@Valid @RequestBody String name, @PathVariable Long id) {
        return projectService.renameProject(name, id);
    }

    @DeleteMapping("/delete/project/{id}")
    ResponseEntity<?> deleteOne(@PathVariable Long id) throws JSONException {
        ResponseEntity<?> responseEntity = projectService.deleteOne(id);
        restTemplate.delete("http://user/delete/project/{id}", id);
        return responseEntity;
    }

    @PostMapping("/addProject")
    ResponseEntity<Project> newProject(@Valid @RequestBody Project newProject) throws URISyntaxException {
        ResponseEntity<Project> projectResponseEntity = projectService.newProject(newProject);
        ResponseEntity<Project> responseEntity = restTemplate.postForEntity("http://user/addUserProject", projectResponseEntity.getBody(), Project.class);
        return projectResponseEntity;
    }

    @GetMapping("/projects")
    ResponseEntity<?> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/project/{id}")
    ResponseEntity<?> getOneProject(@PathVariable Long id) {
        return projectService.getOneProject(id);
    }

    @GetMapping(value = "/filterProjects/{filter}")
    public ResponseEntity filterProjects(@PathVariable String filter) throws JSONException {
        return projectService.getProjectsByFilter(filter);
    }

    @GetMapping(value="/searchProjectsByName/{filter}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity searchProjectsByName(@PathVariable String filter){
        return projectService.searchProjectsByName(filter);
    }
}
