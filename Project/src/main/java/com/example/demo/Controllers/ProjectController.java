package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Repositories.ProjectRepository;
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
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PutMapping("/addOrUpdateProject/{id}")
    ResponseEntity<?> addOrReplace(@RequestBody Project newProject, @PathVariable Long id) {
        Project project = projectRepository.findByID(id);
        if (project != null){
            if(!newProject.getName().isEmpty()) project.setName(newProject.getName());
            if(newProject.getDate_created() != null) project.setDate_created(newProject.getDate_created());
            if(newProject.getDate_modified() != null) project.setDate_modified(newProject.getDate_modified());
            if(!newProject.getPriority().equals("")) project.setPriority(newProject.getPriority());
            projectRepository.save(project);
        }
        else {
            newProject.setID(id);
            projectRepository.save(newProject);
        }
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PutMapping("/renameProject/{id}")
    ResponseEntity<Project> renameProject(@RequestBody String name, @PathVariable Long id) {
        Project p =  projectRepository.findByID(id);

        if(name != ""){
            p.setName(name);
            projectRepository.save(p);
        }

        return new ResponseEntity<Project>(p, HttpStatus.OK);
    }

    @DeleteMapping("/delete/project/{id}")
    ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id){
        projectRepository.deleteById(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addProject")
    ResponseEntity<?> newProject(@RequestBody Project newProject) throws URISyntaxException {
        Project project = projectRepository.save(newProject);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/projects")
    List<Project> all(){
        return projectRepository.findAll();
    }

    @GetMapping("/project/{id}")
    Project one(@PathVariable Long id) {
        return projectRepository.findByID(id);
    }


}
