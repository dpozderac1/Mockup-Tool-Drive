package com.example.online_testing.Services;

import com.example.online_testing.ErrorHandling.AlreadyExistsException;
import com.example.online_testing.ErrorHandling.RecordNotFoundException;
import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.RoleRepository;
import com.example.online_testing.Repositories.UserRepository;
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

    @Override
    public ResponseEntity updateUser(Long id, User user) {
        User korisnik = userRepository.findByID(id);
        JSONObject objekat = new JSONObject();
        if (korisnik == null) {
            throw new RecordNotFoundException("User does not exist!");
        }

        List<User> sviKorisnici = userRepository.findAll();
        for (int i = 0; i < sviKorisnici.size(); i++) {
            User korisnik1 = sviKorisnici.get(i);
            if (korisnik1.getUsername().equals(user.getUsername())) {
                throw new AlreadyExistsException("User with same username already exists!");
            }
            if (korisnik1.getEmail().equals(user.getEmail())) {
                throw new AlreadyExistsException("User with same e-mail address already exists!");
            }
        }

        if(!Integer.toString(user.getIdRole()).equals(Integer.toString(0))) {
            Role uloga = roleRepository.findByID(Long.valueOf(user.getIdRole()));
            if (uloga == null) {
                throw new RecordNotFoundException("Role does not exist!");
            }
            else {
                korisnik.setRoleID(uloga);
            }
        }

        if(user.getRoleID()!=null){
            korisnik.setRoleID(user.getRoleID());
        }
        if (!user.getUsername().isEmpty()) {
            korisnik.setUsername(user.getUsername());
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
            return new ResponseEntity(objekat.toString(), HttpStatus.OK);
        }
        else {
            throw new RecordNotFoundException("User does not exist!");
        }
    }

    @Override
    public ResponseEntity saveUser(User user) {
        JSONObject objekat=new JSONObject();
        if(!Integer.toString(user.getIdRole()).equals(Integer.toString(0))) {
            Role uloga = roleRepository.findByID(Long.valueOf(user.getIdRole()));
            if (uloga == null) {
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
                throw new AlreadyExistsException("User with same username already exists!");
            }
            if(korisnik.getEmail().equals(user.getEmail())){
                throw new AlreadyExistsException("User with same e-mail address already exists!");
            }
        }
        userRepository.save(user);
        try {
            objekat.put("message","User is successfully added!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(user,HttpStatus.CREATED);
    }
}
