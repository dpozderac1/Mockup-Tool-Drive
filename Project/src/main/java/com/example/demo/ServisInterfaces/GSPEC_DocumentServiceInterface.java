package com.example.demo.ServisInterfaces;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GSPEC_DocumentServiceInterface {

    public ResponseEntity addOrReplaceGSPEC(GSPEC_Document newGspec, Long id);
    public ResponseEntity allGSPECsOfMockup(Long id);
    public ResponseEntity deleteOneGSPEC(Long id) throws JSONException;
    public ResponseEntity newGSPEC(GSPEC_Document newGspec);
    public ResponseEntity getAllGSPECs();
    public ResponseEntity getOneGSPEC(Long id);
}
