package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Project;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public interface ProjectServiceInterface {

    public ResponseEntity addOrReplace(Project newProject, Long id);
    public ResponseEntity renameProject(String name, Long id);
    public ResponseEntity deleteOne(Long id) throws JSONException;
    public ResponseEntity newProject(Project newProject, Long id);
    public ResponseEntity getAllProjects();
    public ResponseEntity getOneProject(Long id);
    public ResponseEntity getFilesByFilter(String filter, Long id) throws JSONException;
    public ResponseEntity searchFilesByName(String name, Long id);
    public HashMap<String,Object> getAllUserFiles(Long id);
    public HashMap<String,Object> getRecentUserFiles(Long id);
}
