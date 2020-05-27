package com.example.demo.Services;

import com.example.demo.Models.Project;
import com.example.demo.Models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    ResponseEntity getUserByID(Long id);
    ResponseEntity getUserByUsername(String username);
    List<User> getUsersByRoleID(Long id);
    List<Project> getUserProjects(Long id);
    ResponseEntity saveUser(User user);
    ResponseEntity updateUser(Long id, User user);
    ResponseEntity deleteUser(Long id);
    List<User> getUsersSharingProjectByProjectId(Long id);
    //ResponseEntity getAllProjects(Long id);
}
