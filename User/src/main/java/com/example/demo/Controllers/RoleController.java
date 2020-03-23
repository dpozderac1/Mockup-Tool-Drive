package com.example.demo.Controllers;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class RoleController {
    private RoleRepository roleRepository;

    @Autowired
    public void RoleRepository(RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }

    @GetMapping("/roles")
    List<Role> all(){
        return roleRepository.findAll();
    }

    @GetMapping("/roles/{id}")
    Role id(@PathVariable Long id){
        return roleRepository.findByid(id);
    }

    @PostMapping("/role")
    Role newRole(@RequestBody Role newRole){
        return roleRepository.save(newRole);
    }

    @DeleteMapping("/deleteRole/{id}")
    void deleteRole(@PathVariable Long id) {
        roleRepository.deleteById(id);
    }

    @PutMapping("/updateRole/{id}")
    ResponseEntity updateRole(@PathVariable Long id, @RequestBody Role role){
        Role uloga=roleRepository.findByid(id);
        if(uloga==null){
            return new ResponseEntity("The role you want to update does not exist!", HttpStatus.NOT_FOUND);
        }
        else{
            uloga.setRole_name(role.getRole_name());
            roleRepository.save(uloga);
        }
        return new ResponseEntity(uloga,HttpStatus.OK);
    }
}
