package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface VersionServiceInterface {
    ResponseEntity addOrReplace(Version newVersion, Long id);
    ResponseEntity changeVersion(VersionNames name, Long id);
    ResponseEntity allVersionsOfProject(Long id);
    ResponseEntity deleteOne(Long id) throws JSONException;
    ResponseEntity newVersion(Version newVersion);
    ResponseEntity getAllVersions();
    ResponseEntity getOneVersion(Long id);
}
