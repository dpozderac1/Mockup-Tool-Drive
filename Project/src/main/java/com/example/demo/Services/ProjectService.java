package com.example.demo.Services;

import com.example.demo.Models.Project;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.ServisInterfaces.ProjectServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements ProjectServiceInterface {
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project addOrReplace(Project newProject, Long id){
        Project project = projectRepository.findByID(id);
        if (project != null){
            if(!newProject.getName().isEmpty()) project.setName(newProject.getName());
            if(newProject.getDate_created() != null) project.setDate_created(newProject.getDate_created());
            if(newProject.getDate_modified() != null) project.setDate_modified(newProject.getDate_modified());
            if(!newProject.getPriority().equals("")) project.setPriority(newProject.getPriority());
            projectRepository.save(project);
            return project;
        }
        else {
            newProject.setID(id);
            projectRepository.save(newProject);
            return newProject;
        }
    }

    @Override
    public Project renameProject(String name, Long id){
        Project project =  projectRepository.findByID(id);

        if(name != ""){
            project.setName(name);
            projectRepository.save(project);
        }
        return project;
    }

    @Override
    public void deleteOne(Long id){
        projectRepository.deleteById(id);
    }

    @Override
    public Project newProject(Project newProject){
        Project project = projectRepository.save(newProject);
        return project;
    }

    @Override
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    @Override
    public Project getOneProject(Long id){
        return projectRepository.findByID(id);
    }
}
