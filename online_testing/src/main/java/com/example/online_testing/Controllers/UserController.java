package com.example.online_testing.Controllers;

import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//@CrossOrigin
//@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    List<User> all() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User oneId(@PathVariable Long id) {
        return userRepository.findByID(id);
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id) {
        if(userRepository.existsByID(id)) {
            userRepository.deleteById(id);
            return "User is successfully deleted!\n";
        }
        return "User does not exist!\n";
    }

    /*@PostMapping("/addUser")
    User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }*/

}
