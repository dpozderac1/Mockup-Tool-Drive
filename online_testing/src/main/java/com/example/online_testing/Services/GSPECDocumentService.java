package com.example.online_testing.Services;

import com.example.online_testing.Models.GSPECDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface GSPECDocumentService {

    ResponseEntity deleteGSPECDocumentByID(Long id);
    ResponseEntity updateGSPECDocument(GSPECDocument gspecDocument, Long id);
    ResponseEntity saveGSPECDocument(GSPECDocument gspecDocument);

    ResponseEntity changeGSPECFile (MultipartFile gspecFile, Long id) throws IOException, SQLException;
}
