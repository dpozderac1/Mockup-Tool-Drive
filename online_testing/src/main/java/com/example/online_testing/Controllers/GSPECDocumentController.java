package com.example.online_testing.Controllers;

import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Services.GSPECDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin
@RestController
public class GSPECDocumentController {


    @Autowired
    private GSPECDocumentService gspecDocumentService;

    @DeleteMapping(value = "/GSPECDocument/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity deleteGSPECDocument(@PathVariable Long id) {
        return gspecDocumentService.deleteGSPECDocumentByID(id);
    }

    @PutMapping("/updateGSPECDocument/{id}")
    ResponseEntity updateGSPECDocument (@Valid @RequestBody GSPECDocument gspecDocument, @PathVariable Long id) {
        return gspecDocumentService.updateGSPECDocument(gspecDocument, id);
    }

    @PostMapping("/addGSPECDocument")
    ResponseEntity addGSPECDocument(@Valid @RequestBody GSPECDocument gspecDocument) {
        return gspecDocumentService.saveGSPECDocument(gspecDocument);
    }

}
