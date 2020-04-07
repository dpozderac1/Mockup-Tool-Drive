package com.example.demo.Services;

import com.example.demo.Models.Project;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URISyntaxException;

public interface ProjectService {

    ResponseEntity newProject(Project newProject, Long id) throws URISyntaxException;
    ResponseEntity deleteProject(Long id) throws JSONException;

}
