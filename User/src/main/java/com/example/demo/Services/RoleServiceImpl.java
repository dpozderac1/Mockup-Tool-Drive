package com.example.demo.Services;

import com.example.demo.ErrorHandling.AlreadyExistsException;
import com.example.demo.ErrorHandling.RecordNotFoundException;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.RoleRepository;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public ResponseEntity getRoleByID(Long id) {
        if(roleRepository.existsByID(id)){
            return new ResponseEntity(roleRepository.findByID(id), HttpStatus.OK);
        }
        else{
            throw new RecordNotFoundException("Role does not exist!");
        }
    }

    @Override
    public ResponseEntity saveRole(Role role) {
        List<Role> sveUloge=roleRepository.findAll();
        JSONObject objekat=new JSONObject();
        if(role==null){
            throw new RecordNotFoundException("Role does not exist!");
        }
        for(int i=0;i<sveUloge.size();i++){
            if(role.getRole_name().equals(sveUloge.get(i).getRole_name())){
                throw new AlreadyExistsException("Role already exists!");
            }
        }

        boolean istina=false;
        for(int i=0;i< RoleNames.values().length;i++){
            if(role.getRole_name().toString().equals(RoleNames.values()[i].name().toString())){
                istina=true;
                break;
            }
        }
        if(!istina){
            throw new RecordNotFoundException("Role does not exist!");
        }

        roleRepository.save(role);
        try {
            objekat.put("message","Role is successfully added!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Role> request = new HttpEntity<>(role, headers);
        Role role1 = restTemplate.postForObject("http://online-testing/role", request, Role.class);
        return new ResponseEntity(objekat.toString(),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity deleteRole(Long id) {
        JSONObject objekat=new JSONObject();
        if(roleRepository.existsByID(id)){
            try {
                objekat.put("message","Role is successfully deleted!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            roleRepository.deleteById(id);
            restTemplate.delete("http://online-testing/deleteRole/"+id.toString());
            return new ResponseEntity(objekat.toString(),HttpStatus.OK);
        }
        else {
            throw new RecordNotFoundException("Role does not exist!");
        }
    }

    @Override
    public ResponseEntity updateRole(Long id, Role role) {
        JSONObject objekat=new JSONObject();
        List<Role> sveUloge=roleRepository.findAll();
        if(role==null || !roleRepository.existsByID(id)){
            throw new RecordNotFoundException("Role does not exist!");
        }
        for(int i=0;i<sveUloge.size();i++){
            if(role.getRole_name().equals(sveUloge.get(i).getRole_name())){
                throw new AlreadyExistsException("Role already exists!");
            }
        }

        boolean istina=false;
        for(int i=0;i< RoleNames.values().length;i++){
            if(role.getRole_name().toString().equals(RoleNames.values()[i].name().toString())){
                istina=true;
                break;
            }
        }
        if(!istina){
            throw new RecordNotFoundException("Role does not exist!");
        }


        Role uloga=roleRepository.findByID(id);
        uloga.setRole_name(role.getRole_name());
        roleRepository.save(uloga);

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Role> request=new HttpEntity<>(role,httpHeaders);
        restTemplate.put("http://online-testing/updateRole/"+id.toString(),request);

        return new ResponseEntity(uloga,HttpStatus.OK);
    }
}
