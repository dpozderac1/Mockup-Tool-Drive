package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Project;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProjectServiceInterface {

    public Project addOrReplace(Project newProject, Long id);
    public Project renameProject(String name, Long id);
    public void deleteOne(Long id);
    public Project newProject(Project newProject);
    public List<Project> getAllProjects();
    public Project getOneProject(Long id);
}
