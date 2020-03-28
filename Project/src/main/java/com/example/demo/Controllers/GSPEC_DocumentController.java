package com.example.demo.Controllers;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import com.example.demo.Services.GSPEC_DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class GSPEC_DocumentController {
    private GSPEC_DocumentService gspec_documentService;

    @Autowired
    public GSPEC_DocumentController(GSPEC_DocumentService gspec_documentService) {
        this.gspec_documentService = gspec_documentService;
    }

    @PutMapping("/addOrUpdateGSPEC_Document/{id}")
    ResponseEntity<?> addOrReplaceGSPEC(@RequestBody GSPEC_Document newGspec, @PathVariable Long id) {
        GSPEC_Document gspec_document = gspec_documentService.addOrReplaceGSPEC(newGspec, id);
        return new ResponseEntity<>(gspec_document, HttpStatus.OK);
    }

    @GetMapping("/GSPEC_Documents/mockup/{id}")
    ResponseEntity<?> allGSPECsOfMockup(@PathVariable Long id){
        List<GSPEC_Document> gspec_documents = gspec_documentService.allGSPECsOfMockup(id);
        return new ResponseEntity<>(gspec_documents, HttpStatus.OK);
    }

    @DeleteMapping("/delete/gspec_document/{id}")
    ResponseEntity<?> deleteOneGSPEC(@PathVariable Long id){
        gspec_documentService.deleteOneGSPEC(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addGSPEC_Document")
    ResponseEntity<?> newGSPEC(@RequestBody GSPEC_Document newGspec) {
        GSPEC_Document gspec_document = gspec_documentService.newGSPEC(newGspec);
        return new ResponseEntity<>(gspec_document, HttpStatus.CREATED);
    }

    @GetMapping("/gspec_documents")
    ResponseEntity<?> getAllGSPECs(){
        List<GSPEC_Document> gspec_documents = gspec_documentService.getAllGSPECs();
        return new ResponseEntity<>(gspec_documents, HttpStatus.OK);
    }

    @GetMapping("/gspec_document/{id}")
    ResponseEntity<?> getOneGSPEC(@PathVariable Long id) {
        GSPEC_Document gspec_document = gspec_documentService.getOneGSPEC(id);
        return new ResponseEntity<>(gspec_document, HttpStatus.OK);
    }

}
