package com.example.online_testing.Services;

import com.example.online_testing.Models.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    //kreirano
    ResponseEntity updateUser(Long id, User user);
    ResponseEntity deleteUser(Long id);
    ResponseEntity saveUser(User user);
}
