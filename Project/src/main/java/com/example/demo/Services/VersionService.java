package com.example.demo.Services;

import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.ServisInterfaces.VersionServiceInterface;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Version addOrReplace(Version newVersion, Long id){
        Version version = versionRepository.findByID(id);
        if(version != null){
            if(!newVersion.getVersion_name().equals("")) version.setVersion_name(newVersion.getVersion_name());
            if(newVersion.getProjectId() != null) version.setProjectId(newVersion.getProjectId());
            versionRepository.save(version);
            return version;
        }
        else {
            newVersion.setID(id);
            versionRepository.save(newVersion);
            return newVersion;
        }
    }

    @Override
    public Version changeVersion(VersionNames name, Long id){
        Version version = versionRepository.findByID(id);
        if(!name.equals("")){
            version.setVersion_name(name);
            versionRepository.save(version);
        }
        return version;
    }

    @Override
    public List<Version> allVersionsOfProject(Long id){
        Project project = projectRepository.findByID(id);
        List<Version> versions  = versionRepository.findAllByprojectId(project);

        return versions;
    }

    @Override
    public void deleteOne(Long id){
        versionRepository.deleteById(id);
    }

    @Override
    public Version newVersion(Version newVersion){
        Version version = versionRepository.save(newVersion);
        return version;
    }

    @Override
    public List<Version> getAllVersions(){
        List<Version> versions = versionRepository.findAll();
        return versions;
    }

    @Override
    public Version getOneVersion(Long id){
        Version version = versionRepository.findByID(id);
        return version;
    }

}
