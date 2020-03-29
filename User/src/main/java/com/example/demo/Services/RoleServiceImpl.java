package com.example.demo.Services;

import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Repositories.RoleRepository;
import org.apache.catalina.mbeans.RoleMBean;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public ResponseEntity getRoleById(Long id) {
        JSONObject objekat=new JSONObject();
        if(roleRepository.existsByID(id)){
            return new ResponseEntity(roleRepository.findByID(id), HttpStatus.OK);
        }
        else{
            try {
                objekat.put("message","Role does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity saveRole(Role role) {
        List<Role> sveUloge=roleRepository.findAll();
        JSONObject objekat=new JSONObject();
        if(role==null){
            try {
                objekat.put("message","Role is empty!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
        }
        for(int i=0;i<sveUloge.size();i++){
            if(role.getRole_name()==sveUloge.get(i).getRole_name()){
                try {
                    objekat.put("message","Role with same role name already exists!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity(objekat.toString(),HttpStatus.CONFLICT);
            }
        }

        boolean istina=false;
        for(int i=0;i< RoleNames.values().length;i++){
            if(role.getRole_name().toString()==RoleNames.values()[i].name().toString()){
                istina=true;
                break;
            }
        }
        if(!istina){
            try {
                objekat.put("message","Role name does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
        }

        roleRepository.save(role);
        try {
            objekat.put("message","Role is successfully added!");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(objekat.toString(),HttpStatus.OK);
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
            try {
                objekat.put("message", "Role does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> updateRole(Long id, Role role) {
        JSONObject objekat=new JSONObject();
        List<Role> sveUloge=roleRepository.findAll();
        if(role==null){
            try {
                objekat.put("message","Role is empty!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
        }
        for(int i=0;i<sveUloge.size();i++){
            if(role.getRole_name()==sveUloge.get(i).getRole_name()){
                try {
                    objekat.put("message","Role with same role name already exists!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity(objekat.toString(),HttpStatus.CONFLICT);
            }
        }

        boolean istina=false;
        for(int i=0;i< RoleNames.values().length;i++){
            if(role.getRole_name().toString()==RoleNames.values()[i].name().toString()){
                istina=true;
                break;
            }
        }
        if(!istina){
            try {
                objekat.put("message","Role name does not exist!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(objekat.toString(),HttpStatus.NOT_FOUND);
        }


        Role uloga=roleRepository.findByID(id);
        uloga.setRole_name(role.getRole_name());
        roleRepository.save(uloga);
        try {
            objekat.put ("message","Role is successfully updated!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(objekat.toString(),HttpStatus.OK);
    }
}
