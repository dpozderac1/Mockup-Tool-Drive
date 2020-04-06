package com.example.demo.Services;

import com.example.demo.ErrorMessageHandling.CustomRestExceptionHandler;
import com.example.demo.ErrorMessageHandling.ObjectAlreadyExistsException;
import com.example.demo.ErrorMessageHandling.ObjectNotFoundException;
import com.example.demo.Models.Project;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.ServisInterfaces.ProjectServiceInterface;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProjectService implements ProjectServiceInterface {
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ResponseEntity addOrReplace(Project newProject, Long id){
        Project project = projectRepository.findByID(id);
        if (project != null){
            if(!newProject.getName().isEmpty()) project.setName(newProject.getName());
            if(newProject.getDate_created() != null) project.setDate_created(newProject.getDate_created());
            if(newProject.getDate_modified() != null) project.setDate_modified(newProject.getDate_modified());
            if(!newProject.getPriority().equals("")) project.setPriority(newProject.getPriority());
            projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        }
        else {
            newProject.setID(id);
            projectRepository.save(newProject);
            return new ResponseEntity<>(newProject, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity renameProject(String name, Long id){
        Project project =  projectRepository.findByID(id);
        if(project != null){
            project.setName(name);
            projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        }
        else{
            throw new ObjectNotFoundException("Project with id " + id + " does not exist!");
        }
    }

    @Override
    public ResponseEntity deleteOne(Long id) throws JSONException {
        if(projectRepository.existsByID(id)){
            projectRepository.deleteById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","Project successfully deleted!");
            return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("Project with id " + id + " does not exit!");
    }

    @Override
    public ResponseEntity newProject(Project newProject){
        List<Project> projects  = projectRepository.findAll();
        boolean alreadyExists = false;
        for(Project p: projects){
            if(p.getID().equals(newProject.getID())) alreadyExists = true;
        }
        if(!alreadyExists){
            Project project = projectRepository.save(newProject);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        }
        else
            throw new ObjectAlreadyExistsException("Project with id " + newProject.getID() + " already exists!");
    }

    @Override
    public ResponseEntity getAllProjects(){
        List<Project> projects = projectRepository.findAll();
        if(projects != null){
            return new ResponseEntity<>(projects, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("Projects not found!");
    }

    @Override
    public ResponseEntity getOneProject(Long id){
        Project project = projectRepository.findByID(id);
        if(project != null){
            return new ResponseEntity<>(project, HttpStatus.OK);
        }
        else{
            throw new ObjectNotFoundException("Project with id " + id + " does not exist!");
        }
    }

    @Override
    public ResponseEntity getProjectsByFilter(String filter) throws JSONException {
        List<Project> projects = projectRepository.findAll();
        if(filter.equals("datum_kreiranja")){
            Collections.sort(projects, new Comparator<Project>() {
                public int compare(Project p1, Project p2) {
                    return p1.getDate_created().compareTo(p2.getDate_created());
                }
            });
            return new ResponseEntity<>(projects, HttpStatus.OK);
        }
        else if(filter.equals("datum_modifikovanja")){
            Collections.sort(projects, new Comparator<Project>() {
                public int compare(Project p1, Project p2) {
                    return p1.getDate_modified().compareTo(p2.getDate_modified());
                }
            });
            return new ResponseEntity<>(projects, HttpStatus.OK);
        }
        else if(filter.equals("naziv")){
            Collections.sort(projects, new Comparator<Project>() {
                public int compare(Project p1, Project p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            return new ResponseEntity<>(projects, HttpStatus.OK);
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","Filter is not defined!");
            return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity searchProjectsByName(String name){
        List<Project> projects = projectRepository.findAll();
        List<Project> finalProjects = new ArrayList<>();
        for (Project p: projects) {
            if(p.getName().equals(name))
                finalProjects.add(p);
        }
        return new ResponseEntity<>(finalProjects, HttpStatus.OK);
    }
}
