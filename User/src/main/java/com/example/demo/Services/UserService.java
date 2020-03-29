package com.example.demo.Services;

import com.example.demo.Models.Project;
import com.example.demo.Models.User;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    ResponseEntity getUserByID(Long id);
    List<User> getUsersByRoleID(Long id);
    List<Project> getUserProjects(Long id);
    ResponseEntity saveUser(User user);
    ResponseEntity updateUser(Long id, User user);
    ResponseEntity deleteUser(Long id);
}