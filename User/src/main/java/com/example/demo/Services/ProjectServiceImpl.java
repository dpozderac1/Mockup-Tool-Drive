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

import java.net.URISyntaxException;
import java.util.List;

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

    @Override
    public ResponseEntity addProjectToUser(Long id, Project project) {
        if(!userRepository.existsByID(id)){
            throw new RecordNotFoundException("User with id " + id + " does not exist");
        }
        else if(!projectRepository.existsByID(project.getID())){
            throw new RecordNotFoundException("Project with id " + project.getID() + " does not exist");
        }
        else{
            Project projekat=projectRepository.findByID(project.getID());
            User korisnik=userRepository.findByID(id);
            List<Project> projekti=korisnik.getProjects();
            projekti.add(projekat);
            korisnik.setProjects(projekti);

            /*List<User> korisnici=projekat.getUsers();
            korisnici.add(korisnik);
            projekat.setUsers(korisnici);*/
            userRepository.save(korisnik);
            //projectRepository.save(projekat);
            return new ResponseEntity<>(projekat, HttpStatus.CREATED);
        }
    }


}
