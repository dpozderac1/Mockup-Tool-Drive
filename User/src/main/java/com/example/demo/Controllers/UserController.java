package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value="/users",produces = MediaType.APPLICATION_JSON_VALUE)
    List<User> all(){
        return userService.getAllUsers();
    }

    @GetMapping(value="/users/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getId(@PathVariable Long id){
        return userService.getUserByID(id);
    }

    @GetMapping(value="/users/role/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    List<User> getByRoleId(@PathVariable Long id){
        return userService.getUsersByRoleID(id);
    }

    @GetMapping(value="/users/projects/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    List<Project> getByProjectId(@PathVariable Long id){
        return userService.getUserProjects(id);
    }

    @PostMapping(value="/user",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity newUser(@Valid @RequestBody User newUser){
        return userService.saveUser(newUser);
    }

    @DeleteMapping(value="/deleteUser/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping(value="/updateUser/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity updateUser(@Valid @RequestBody User user,@PathVariable Long id){
        return userService.updateUser(id,user);
    }

    @GetMapping(value="/users/username/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    /*@GetMapping(value="/user/allFiles/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAllProjects(@PathVariable Long id) {
        return userService.getAllProjects(id);
    }*/
}
