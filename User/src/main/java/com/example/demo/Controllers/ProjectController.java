package com.example.demo.Controllers;

import com.example.demo.ErrorHandling.AlreadyExistsException;
import com.example.demo.ErrorHandling.ApiError;
import com.example.demo.Models.Project;
import com.example.demo.Services.ProjectService;
import com.example.demo.Services.ProjectServiceImpl;
import com.example.demo.Services.RoleService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;

@CrossOrigin
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/addUserProject")
    ResponseEntity<Project> newProject(@RequestBody Map<Long, Project> newProject) throws URISyntaxException {
        return projectService.newProject(newProject.entrySet().iterator().next().getValue(), newProject.entrySet().iterator().next().getKey() );
    }

    @DeleteMapping("/delete/project/{id}")
    ResponseEntity deleteProject(@PathVariable Long id) throws JSONException {
        return projectService.deleteProject(id);
    }
}
