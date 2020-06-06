package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Mockup;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface MockupServiceInterface {
    public ResponseEntity addOrReplace(Mockup newMockup, Long id);
    public ResponseEntity allMockupsOfVersion(Long id);
    public ResponseEntity deleteOne(Long id) throws JSONException;
    public ResponseEntity newMockup(Mockup newMockup);
    public ResponseEntity getAllMockups();
    public ResponseEntity getOneMockup(Long id);

    public ResponseEntity addOrUpdateFile(MultipartFile fajl, Long id) throws IOException, SQLException;
    public ResponseEntity getOneFile(Long id) throws SQLException;
}
