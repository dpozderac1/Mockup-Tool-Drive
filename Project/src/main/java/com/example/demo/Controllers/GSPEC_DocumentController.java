package com.example.demo.Controllers;

import com.example.demo.RabbitMQ.MessageRabbitMq;
import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Services.GSPEC_DocumentService;
import com.example.demo.RabbitMQ.BindingInterfaceInput;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@EnableBinding(BindingInterfaceInput.class)
//@CrossOrigin
@RestController
public class GSPEC_DocumentController {

    private GSPEC_DocumentService gspec_documentService;


    @Autowired
    public GSPEC_DocumentController(GSPEC_DocumentService gspec_documentService) {
        this.gspec_documentService = gspec_documentService;

    }

    //RabbitMQ
    @DeleteMapping("/delete/gspec_document/{id}")
    ResponseEntity deleteOneGSPEC(@PathVariable Long id) throws JSONException {
        return gspec_documentService.deleteOneGSPEC(id);
    }

    @PutMapping("/addOrUpdateGSPEC_Document/{id}")
    ResponseEntity<?> addOrReplaceGSPEC(@Valid @RequestBody GSPEC_Document newGspec, @PathVariable Long id) {
        return gspec_documentService.addOrReplaceGSPEC(newGspec, id);
    }

    @GetMapping("/GSPEC_Documents/mockup/{id}")
    ResponseEntity<?> allGSPECsOfMockup(@PathVariable Long id){
        return gspec_documentService.allGSPECsOfMockup(id);
    }



    @PostMapping("/addGSPEC_Document")
    ResponseEntity<?> newGSPEC(@RequestBody GSPEC_Document newGspec) {
        return gspec_documentService.newGSPEC(newGspec);
    }

    @GetMapping("/gspec_documents")
    ResponseEntity<?> getAllGSPECs(){
        return gspec_documentService.getAllGSPECs();
    }

    @GetMapping("/gspec_document/{id}")
    ResponseEntity<?> getOneGSPEC(@PathVariable Long id) {
        return gspec_documentService.getOneGSPEC(id);

    }

}
