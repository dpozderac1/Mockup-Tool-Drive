package com.example.online_testing.Controllers;


import com.example.online_testing.Models.Role;
import com.example.online_testing.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@CrossOrigin
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping(value="/role",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity newRole(@Valid @RequestBody Role newRole){
        return roleService.saveRole(newRole);
    }

    @DeleteMapping(value="/deleteRole/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @PutMapping(value="/updateRole/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRole(@Valid @PathVariable Long id, @RequestBody Role role){
        return roleService.updateRole(id,role);
    }
}
