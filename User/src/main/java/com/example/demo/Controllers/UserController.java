package com.example.demo.Controllers;

import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository,RoleRepository roleRepository){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
    }

    @GetMapping("/users")
    List<User> all(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    User getId(@PathVariable Long id){
        return userRepository.findByID(id);
    }

    @GetMapping("/users/role/{id}")
    List<User> getByRoleId(@PathVariable Long id){
        Role uloga = roleRepository.findByID(id);
        List<User> korisnici = userRepository.findByroleID(uloga);
        return korisnici;
        /*List<User> korisnici=userRepository.findAll();
        List<User> korisniciPoUlogama=new ArrayList<User>();
        for(int i=0;i<korisnici.size();i++){
            User korisnik= korisnici.get(i);
            if(korisnik.getRole_id().getId()==id){
                korisniciPoUlogama.add(korisnik);
            }
        }
        return korisniciPoUlogama;*/
    }

    @GetMapping("/users/projects/{id}")
    List<Project> getByProjectId(@PathVariable Long id){
        User korisnik=userRepository.findByID(id);
        System.out.println("ISPIS"+korisnik.getID());
        List<Project> projekti=korisnik.getProjects();
        return projekti;
    }

    @PostMapping("/user")
    User newUser(@RequestBody User newUser){
        return userRepository.save(newUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.findByID(id);
    }

    @PutMapping("/updateUser/{id}")
    ResponseEntity updateUser(@PathVariable Long id, @RequestBody User user){
        User korisnik=userRepository.findByID(id);
        if(korisnik==null){
            return new ResponseEntity("The user you want to update does not exist!", HttpStatus.NOT_FOUND);
        }
        else{
            if(!user.getName().isEmpty()){
                korisnik.setName(user.getName());
            }
            if(!user.getSurname().isEmpty()){
                korisnik.setSurname(user.getSurname());
            }
            if(!user.getUsername().isEmpty()){
                korisnik.setUsername(user.getUsername());
            }
            if(!user.getPassword().isEmpty()){
                korisnik.setPassword(user.getPassword());
            }
            if(!user.getEmail().isEmpty()){
                korisnik.setEmail(user.getEmail());
            }

            userRepository.save(korisnik);
        }
        return new ResponseEntity(korisnik,HttpStatus.OK);
    }
}
