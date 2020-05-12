package com.example.online_testing.Services;

import com.example.online_testing.Models.Role;
import org.springframework.http.ResponseEntity;

public interface RoleService {

    ResponseEntity saveRole(Role role);
    ResponseEntity deleteRole(Long id);
    ResponseEntity updateRole(Long id, Role role);
}
