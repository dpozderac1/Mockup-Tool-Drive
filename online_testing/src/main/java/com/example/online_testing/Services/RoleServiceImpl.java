package com.example.online_testing.Services;

import com.example.online_testing.ErrorHandling.AlreadyExistsException;
import com.example.online_testing.ErrorHandling.RecordNotFoundException;
import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Repositories.RoleRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

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
        for(int i = 0; i< RoleNames.values().length; i++){
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

        return new ResponseEntity(objekat.toString(), HttpStatus.CREATED);
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

        return new ResponseEntity(uloga,HttpStatus.OK);
    }
}
