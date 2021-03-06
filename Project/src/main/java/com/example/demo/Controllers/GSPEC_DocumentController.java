package com.example.demo.Controllers;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Services.GSPEC_DocumentService;
import com.example.demo.RabbitMQ.BindingInterfaceInput;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@EnableBinding(BindingInterfaceInput.class)


@RestController
public class GSPEC_DocumentController {

    private GSPEC_DocumentService gspec_documentService;


    @Autowired
    public GSPEC_DocumentController(GSPEC_DocumentService gspec_documentService) {
        this.gspec_documentService = gspec_documentService;

    }

    //RabbitMQ
    @DeleteMapping("/delete/gspec_document/{id}")
    public ResponseEntity deleteOneGSPEC(@PathVariable Long id) throws JSONException {
        return gspec_documentService.deleteOneGSPEC(id);
    }

    @PutMapping("/addOrUpdateGSPEC_Document/{id}")
    public ResponseEntity<?> addOrReplaceGSPEC(@Valid @RequestBody GSPEC_Document newGspec, @PathVariable Long id) {
        return gspec_documentService.addOrReplaceGSPEC(newGspec, id);
    }

    @GetMapping("/GSPEC_Documents/mockup/{id}")
    public ResponseEntity<?> allGSPECsOfMockup(@PathVariable Long id){
        return gspec_documentService.allGSPECsOfMockup(id);
    }



    @PostMapping("/addGSPEC_Document")
    public ResponseEntity<?> newGSPEC(@RequestBody GSPEC_Document newGspec) {
        return gspec_documentService.newGSPEC(newGspec);
    }

    @GetMapping("/gspec_documents")
    public ResponseEntity<?> getAllGSPECs(){
        return gspec_documentService.getAllGSPECs();
    }

    @GetMapping("/gspec_document/{id}")
    public ResponseEntity<?> getOneGSPEC(@PathVariable Long id) {
        return gspec_documentService.getOneGSPEC(id);

    }

    @PostMapping(value="/addGSPECFile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addGSPECFile(@PathVariable Long id, @RequestParam("gspecFile") MultipartFile gspecFajl, @RequestParam("name") String naziv, HttpServletRequest request) throws IOException, SQLException {
        return gspec_documentService.addGSPECFile(id,gspecFajl,naziv,request);
    }

    @GetMapping("/gspec_document/file/{id}")
    public ResponseEntity<?> getOneGSPECFile(@PathVariable Long id) throws SQLException {
        return gspec_documentService.getOneGSPECFile(id);
    }
}
