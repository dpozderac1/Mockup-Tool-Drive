package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface VersionServiceInterface {
    public Version addOrReplace(Version newVersion, Long id);
    public Version changeVersion(VersionNames name, Long id);
    public List<Version> allVersionsOfProject(Long id);
    public void deleteOne(Long id);
    public Version newVersion(Version newVersion);
    public List<Version> getAllVersions();
    public Version getOneVersion(Long id);
}
