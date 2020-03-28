package com.example.demo.ServisInterfaces;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GSPEC_DocumentServiceInterface {

    public GSPEC_Document addOrReplaceGSPEC(GSPEC_Document newGspec, Long id);
    public List<GSPEC_Document> allGSPECsOfMockup(Long id);
    public void deleteOneGSPEC(Long id);
    public GSPEC_Document newGSPEC(GSPEC_Document newGspec);
    public List<GSPEC_Document> getAllGSPECs();
    public GSPEC_Document getOneGSPEC(Long id);
}
