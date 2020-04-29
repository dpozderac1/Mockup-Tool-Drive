package com.example.demo.Services;

import com.example.demo.ErrorHandling.AlreadyExistsException;
import com.example.demo.ErrorHandling.RecordNotFoundException;
import com.example.demo.Models.Project;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.UserRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URISyntaxException;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity newProject(Project newProject, Long id) throws URISyntaxException{
        User user = userRepository.findByID(id);
        if(user == null)
            throw new RecordNotFoundException("User with id " + id + " does not exist");
        else{
            if(projectRepository.existsByID(newProject.getID()))
                throw new AlreadyExistsException("Project with id " + newProject.getID() + " already exists!");
            else{
                newProject.getUsers().add(user);
                projectRepository.save(newProject);
                return new ResponseEntity<>(newProject, HttpStatus.CREATED);
            }
        }
    }

    @Override
    public ResponseEntity deleteProject(Long id) throws JSONException{
        if(projectRepository.existsByID(id)){
            projectRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            throw new RecordNotFoundException("Project with id " + id + " does not exist!");
    }

}