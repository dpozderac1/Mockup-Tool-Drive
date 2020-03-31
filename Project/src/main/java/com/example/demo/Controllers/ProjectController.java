package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Services.ProjectService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import javax.xml.ws.Response;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PutMapping("/addOrUpdateProject/{id}")
    ResponseEntity<?> addOrReplace(@RequestBody Project newProject, @PathVariable Long id) {
        return projectService.addOrReplace(newProject, id);
    }

    @PutMapping("/renameProject/{id}")
    ResponseEntity<?> renameProject(@RequestBody String name, @PathVariable Long id) {
        return projectService.renameProject(name, id);
    }

    @DeleteMapping("/delete/project/{id}")
    ResponseEntity<?> deleteOne(@PathVariable Long id) throws JSONException {
        return projectService.deleteOne(id);
    }

    @PostMapping("/addProject")
    ResponseEntity<Project> newProject(@RequestBody Project newProject) throws URISyntaxException {
        return projectService.newProject(newProject);
    }

    @GetMapping("/projects")
    ResponseEntity<?> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/project/{id}")
    ResponseEntity<?> getOneProject(@PathVariable Long id) {
        return projectService.getOneProject(id);
    }


}
