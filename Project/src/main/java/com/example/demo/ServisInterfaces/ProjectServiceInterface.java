package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Project;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProjectServiceInterface {

    public ResponseEntity addOrReplace(Project newProject, Long id);
    public ResponseEntity renameProject(String name, Long id);
    public ResponseEntity deleteOne(Long id) throws JSONException;
    public ResponseEntity newProject(Project newProject);
    public ResponseEntity getAllProjects();
    public ResponseEntity getOneProject(Long id);
    public ResponseEntity getProjectsByFilter(String filter) throws JSONException;
    public ResponseEntity searchProjectsByName(String name);
}
