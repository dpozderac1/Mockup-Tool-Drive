package com.example.demo.Services;

import com.example.demo.ErrorMessageHandling.ObjectAlreadyExistsException;
import com.example.demo.ErrorMessageHandling.ObjectNotFoundException;
import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.ServisInterfaces.VersionServiceInterface;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionService implements VersionServiceInterface {
    private VersionRepository versionRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public VersionService(VersionRepository versionRepository, ProjectRepository projectRepository) {
        this.versionRepository = versionRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ResponseEntity addOrReplace(Version newVersion, Long id){
        Version version = versionRepository.findByID(id);
        if(version != null){
            if(!newVersion.getVersion_name().equals("")) version.setVersion_name(newVersion.getVersion_name());
            if(newVersion.getProjectId() != null) version.setProjectId(newVersion.getProjectId());
            versionRepository.save(version);
            return new ResponseEntity<>(version, HttpStatus.OK);
        }
        else {
            newVersion.setID(id);
            versionRepository.save(newVersion);
            return new ResponseEntity<>(newVersion, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity changeVersion(VersionNames name, Long id){
        Version version = versionRepository.findByID(id);
        if(!name.equals(" ")){
            version.setVersion_name(name);
            versionRepository.save(version);
            return new ResponseEntity<>(version, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("Version name can not be empty!");
    }

    @Override
    public ResponseEntity allVersionsOfProject(Long id){
        Project project = projectRepository.findByID(id);
        if(project != null){
            List<Version> versions  = versionRepository.findAllByprojectId(project);
            if(versions != null)
                return new ResponseEntity<>(versions, HttpStatus.OK);
            else
                throw new ObjectNotFoundException("Versions with project with id " + id + "do not exist!");
        }
        else
            throw new ObjectNotFoundException("Project with id " + id + "does not exist!");

    }

    @Override
    public ResponseEntity deleteOne(Long id) throws JSONException {
        if(versionRepository.existsByID(id)){
            versionRepository.deleteByID(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","Project successfully deleted!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("Version with id " + id + "does not exist!");
    }

    @Override
    public ResponseEntity newVersion(Version newVersion){
        List<Version> versions  = versionRepository.findAll();
        boolean alreadyExists = false;
        for(Version p: versions){
            if(p.getID().equals(newVersion.getID())) alreadyExists = true;
        }
        if(!alreadyExists){
            Version version = versionRepository.save(newVersion);
            return new ResponseEntity<>(version, HttpStatus.CREATED);
        }
        else
            throw new ObjectAlreadyExistsException("Version with id " + newVersion.getID() + " already exists!");
    }

    @Override
    public ResponseEntity getAllVersions(){
        List<Version> versions = versionRepository.findAll();
        if(versions != null)
            return new ResponseEntity<>(versions, HttpStatus.OK);
        else
            throw new ObjectNotFoundException("Versions do not exist!");
    }

    @Override
    public ResponseEntity getOneVersion(Long id){
        Version version = versionRepository.findByID(id);
        if(version != null)
            return new ResponseEntity<>(version, HttpStatus.OK);
        else
            throw new ObjectNotFoundException("Version with id " + id + "does not exist!");
    }

}
