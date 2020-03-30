package com.example.demo.Services;

import com.example.demo.ErrorHandling.AlreadyExistsException;
import com.example.demo.ErrorHandling.RecordNotFoundException;
import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity getUserByID(Long id) {
        if(userRepository.existsByID(id)){
            return new ResponseEntity(userRepository.findByID(id),HttpStatus.OK);
        }
        else{
            throw new RecordNotFoundException("User does not exist!");
        }
    }

    @Override
    public List<User> getUsersByRoleID(Long id) {
        Role uloga = roleRepository.findByID(id);
        List<User> korisnici = userRepository.findByroleID(uloga);
        return korisnici;
    }

    @Override
    public List<Project> getUserProjects(Long id) {
        User korisnik=userRepository.findByID(id);
        List<Project> projekti=korisnik.getProjects();
        return projekti;
    }

    @Override
    public ResponseEntity saveUser(User user) {
        JSONObject objekat=new JSONObject();
        if(user.getRoleID()==null){
            throw new RecordNotFoundException("Role does not exist!");
        }
        List<User> sviKorisnici=userRepository.findAll();
        for(int i=0;i<sviKorisnici.size();i++){
            User korisnik=sviKorisnici.get(i);
            if(korisnik.getUsername()==user.getUsername()){
                throw new AlreadyExistsException("User with same username already exists!");
            }
            if(korisnik.getEmail()==user.getEmail()){
                throw new AlreadyExistsException("User with same e-mail address already exists!");
            }
        }
        userRepository.save(user);
        try {
            objekat.put("message","User is successfully added!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(objekat.toString(),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity updateUser(Long id, User user) {
        User korisnik = userRepository.findByID(id);
        JSONObject objekat = new JSONObject();
        if (korisnik == null) {
            throw new RecordNotFoundException("User does not exist!");
        }
        if (user.getRoleID() == null) {
            throw new RecordNotFoundException("Role does not exist!");
        }
        List<User> sviKorisnici = userRepository.findAll();
        for (int i = 0; i < sviKorisnici.size(); i++) {
            User korisnik1 = sviKorisnici.get(i);
            if (korisnik1.getUsername() == user.getUsername()) {
                throw new AlreadyExistsException("User with same username already exists!");
            }
            if (korisnik1.getEmail() == user.getEmail()) {
                throw new AlreadyExistsException("User with same e-mail address already exists!");
            }
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

        userRepository.save(korisnik);

        return new ResponseEntity(korisnik, HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteUser(Long id) {
        JSONObject objekat=new JSONObject();
        if(userRepository.existsByID(id)){
            userRepository.deleteById(id);
            try {
                objekat.put("message","User is successfully deleted!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.OK);
        }
        else {
            throw new RecordNotFoundException("User does not exist!");
        }
    }
}
