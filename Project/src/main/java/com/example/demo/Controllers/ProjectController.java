package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Services.ProjectService;
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
        Project project = projectService.addOrReplace(newProject, id);
        if(project != null) return new ResponseEntity<>(project, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/renameProject/{id}")
    ResponseEntity<?> renameProject(@RequestBody String name, @PathVariable Long id) {
        Project project =  projectService.renameProject(name, id);
        if(project != null) return new ResponseEntity<>(project, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/project/{id}")
    ResponseEntity<?> deleteOne(@PathVariable Long id){
        projectService.deleteOne(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addProject")
    ResponseEntity<?> newProject(@RequestBody Project newProject) throws URISyntaxException {
        Project project = projectService.newProject(newProject);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/projects")
    ResponseEntity<?> getAllProjects(){
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/project/{id}")
    ResponseEntity<?> getOneProject(@PathVariable Long id) {
        Project project = projectService.getOneProject(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }


}
