package com.example.online_testing.Services;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.GSPECDocument;
import org.springframework.http.ResponseEntity;

public interface GSPECDocumentService {
    //kreirano
    ResponseEntity deleteGSPECDocumentByID(Long id);
    ResponseEntity updateGSPECDocument(GSPECDocument gspecDocument, Long id);
    ResponseEntity saveGSPECDocument(GSPECDocument gspecDocument);
}
