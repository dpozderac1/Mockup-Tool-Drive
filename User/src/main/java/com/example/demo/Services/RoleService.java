package com.example.demo.Services;

import com.example.demo.Models.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    ResponseEntity getRoleById(Long id);
    ResponseEntity saveRole(Role role);
    ResponseEntity deleteRole(Long id);
    ResponseEntity updateRole(Long id,Role role);
}