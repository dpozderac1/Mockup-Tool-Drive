package com.example.demo.Services;

import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;

import com.sun.org.apache.regexp.internal.RE;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
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
            JSONObject objekat = new JSONObject();
            try {
                objekat.put("message","User does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
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
        System.out.println("ISPIS"+korisnik.getID());
        List<Project> projekti=korisnik.getProjects();
        return projekti;
    }

    @Override
    public ResponseEntity saveUser(User user) {
        JSONObject objekat=new JSONObject();
        if(user.getRoleID()==null){
            try {
                objekat.put("message","Role does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
        }
        List<User> sviKorisnici=userRepository.findAll();
        for(int i=0;i<sviKorisnici.size();i++){
            User korisnik=sviKorisnici.get(i);
            if(korisnik.getUsername()==user.getUsername()){
                try {
                    objekat.put("message","User with same username already exists!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity(objekat.toString(), HttpStatus.CONFLICT);
            }
            if(korisnik.getEmail()==user.getEmail()){
                try {
                    objekat.put("message","User with same e-mail address already exists!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity(objekat.toString(), HttpStatus.CONFLICT);
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
            try {
                objekat.put("message", "User does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(), HttpStatus.NOT_FOUND);
        }
        if (user.getRoleID() == null) {
            try {
                objekat.put("message", "Role does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(), HttpStatus.NOT_FOUND);
        }
        List<User> sviKorisnici = userRepository.findAll();
        for (int i = 0; i < sviKorisnici.size(); i++) {
            User korisnik1 = sviKorisnici.get(i);
            if (korisnik1.getUsername() == user.getUsername()) {
                try {
                    objekat.put("message", "User with same username already exists!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity(objekat.toString(), HttpStatus.CONFLICT);
            }
            if (korisnik1.getEmail() == user.getEmail()) {
                try {
                    objekat.put("message", "User with same e-mail address already exists!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity(objekat.toString(), HttpStatus.CONFLICT);
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
            try {
                objekat.put("message", "User does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(), HttpStatus.NOT_FOUND);
        }
    }
}
