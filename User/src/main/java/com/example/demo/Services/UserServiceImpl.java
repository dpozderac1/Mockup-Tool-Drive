package com.example.demo.Services;

import com.example.demo.ErrorHandling.AlreadyExistsException;
import com.example.demo.ErrorHandling.RecordNotFoundException;
import com.example.demo.GRPCUserService;
import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.coyote.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    GRPCUserService grpcUserService;

    @Override
    public List<User> getAllUsers() {
        grpcUserService.action("user","GET","/users","SUCESS", new Timestamp(System.currentTimeMillis()));
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity getUserByID(Long id) {
        if(userRepository.existsByID(id)){
            grpcUserService.action("user","GET","/users/{id}","SUCESS", new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity(userRepository.findByID(id),HttpStatus.OK);
        }
        else{
            grpcUserService.action("user","GET","/users/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("User does not exist!");
        }
    }

    @Override
    public ResponseEntity getUserByUsername(String username) {
        if(userRepository.existsByUsername(username)){
            grpcUserService.action("user","GET","/user/username/{username}","SUCESS", new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity(userRepository.findByUsername(username),HttpStatus.OK);
        }
        else{
            grpcUserService.action("user","GET","/user/username/{username}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("User does not exist!");
        }
    }

    @Override
    public List<User> getUsersByRoleID(Long id) {
        if(roleRepository.existsByID(id)){
            Role uloga = roleRepository.findByID(id);
            List<User> korisnici = userRepository.findByroleID(uloga);
            grpcUserService.action("user","GET","/users/role/{id}","SUCESS", new Timestamp(System.currentTimeMillis()));
            return korisnici;
        }
        else{
            grpcUserService.action("user","GET","/users/role/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("User does not exist!");
        }
    }

    @Override
    public List<Project> getUserProjects(Long id) {
        if(userRepository.existsByID(id)){
            User korisnik=userRepository.findByID(id);
            List<Project> projekti=korisnik.getProjects();
            grpcUserService.action("user","GET","/users/projects/{id}","SUCESS", new Timestamp(System.currentTimeMillis()));
            return projekti;
        }
        else {
            grpcUserService.action("user","GET","/users/projects/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("User does not exist!");
        }
    }

    @Override
    public ResponseEntity saveUser(User user) {

        JSONObject objekat=new JSONObject();
        if(!Integer.toString(user.getIdRole()).equals(Integer.toString(0))) {
            Role uloga = roleRepository.findByID(Long.valueOf(user.getIdRole()));
            if (uloga == null) {
                grpcUserService.action("user","POST","/user","NOT FOUND", new Timestamp(System.currentTimeMillis()));
                throw new RecordNotFoundException("Role does not exist!");
            }
            else {
                user.setRoleID(uloga);
            }
        }
        List<User> sviKorisnici=userRepository.findAll();
        for(int i=0;i<sviKorisnici.size();i++){
            User korisnik=sviKorisnici.get(i);
            if(korisnik.getUsername().equals(user.getUsername())){
                grpcUserService.action("user","POST","/user","CONFLICT", new Timestamp(System.currentTimeMillis()));
                throw new AlreadyExistsException("User with same username already exists!");
            }
            if(korisnik.getEmail().equals(user.getEmail())){
                grpcUserService.action("user","POST","/user","CONFLICT", new Timestamp(System.currentTimeMillis()));
                throw new AlreadyExistsException("User with same e-mail address already exists!");
            }
        }
        userRepository.save(user);
        try {
            objekat.put("message","User is successfully added!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        User user1 = restTemplate.postForObject("http://online-testing/user", request, User.class);
        grpcUserService.action("user","POST","/user","SUCCESS", new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity(user,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity updateUser(Long id, User user) {
        User korisnik = userRepository.findByID(id);
        JSONObject objekat = new JSONObject();
        if (korisnik == null || !userRepository.existsByID(id)) {
            grpcUserService.action("user","PUT","/updateUser/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("User does not exist!");
        }

        List<User> sviKorisnici = userRepository.findAll();
        for (int i = 0; i < sviKorisnici.size(); i++) {
            User korisnik1 = sviKorisnici.get(i);
            if (korisnik1.getUsername().equals(user.getUsername())) {
                grpcUserService.action("user","PUT","/updateUser/{id}","CONFLICT", new Timestamp(System.currentTimeMillis()));
                System.out.println("Tu sam!");
                throw new AlreadyExistsException("User with same username already exists!");
            }
            if (korisnik1.getEmail().equals(user.getEmail())) {
                grpcUserService.action("user","PUT","/updateUser/{id}","CONFLICT", new Timestamp(System.currentTimeMillis()));
                throw new AlreadyExistsException("User with same e-mail address already exists!");
            }
        }

        if(!Integer.toString(user.getIdRole()).equals(Integer.toString(0))) {
            Role uloga = roleRepository.findByID(Long.valueOf(user.getIdRole()));
            if (uloga == null) {
                grpcUserService.action("user","PUT","/updateUser/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
                throw new RecordNotFoundException("Role does not exist!");
            }
            else {
                korisnik.setRoleID(uloga);
            }
        }

        if(user.getRoleID()!=null){
            korisnik.setRoleID(user.getRoleID());
        }
        if (!user.getName().isEmpty()) {
            korisnik.setName(user.getName());
        }
        if (!user.getSurname().isEmpty()) {
            korisnik.setSurname(user.getSurname());
        }
        if (!user.getUsername().isEmpty()) {
            korisnik.setUsername(user.getUsername());
        }
        if (!user.getPassword().isEmpty()) {
            korisnik.setPassword(user.getPassword());
        }
        if (!user.getEmail().isEmpty()) {
            korisnik.setEmail(user.getEmail());
        }

        //azuriraj i u Online testiranje
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request=new HttpEntity<>(user,httpHeaders);
        restTemplate.put("http://online-testing/updateUser/"+id.toString(),request);

        userRepository.save(korisnik);


        try {
            objekat.put("message","User is successfully updated!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        grpcUserService.action("user","PUT","/updateUser/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(korisnik, HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteUser(Long id) {
        JSONObject objekat=new JSONObject();
        if(userRepository.existsByID(id)){
            List<Project> projekti=userRepository.findByID(id).getProjects();
            for(int i=0;i<projekti.size();i++){
                restTemplate.delete("http://project-client-service/delete/project/"+projekti.get(i).getID().toString());
            }

            userRepository.deleteById(id);
            try {
                objekat.put("message","User is successfully deleted!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            restTemplate.delete("http://online-testing/deleteUser/"+id.toString());
            grpcUserService.action("user","DELETE","/deleteUser/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity(objekat.toString(),HttpStatus.OK);
        }
        else {
            grpcUserService.action("user","DELETE","/deleteUser/{id}","NOT FOUND", new Timestamp(System.currentTimeMillis()));
            throw new RecordNotFoundException("User does not exist!");
        }
    }
}