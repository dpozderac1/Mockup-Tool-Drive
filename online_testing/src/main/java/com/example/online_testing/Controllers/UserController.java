package com.example.online_testing.Controllers;

import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.UserRepository;
import com.example.online_testing.Services.UserService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @DeleteMapping(value="/deleteUser/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping(value="/updateUser/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity updateUser(@Valid @PathVariable Long id, @RequestBody User user){
        return userService.updateUser(id,user);
    }

    @PostMapping(value="/user")
    ResponseEntity newUser(@Valid @RequestBody User newUser){
        return userService.saveUser(newUser);
    }

}
