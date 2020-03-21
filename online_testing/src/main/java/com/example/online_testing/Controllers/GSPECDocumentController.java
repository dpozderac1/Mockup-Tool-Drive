package com.example.online_testing.Controllers;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Repositories.GSPECDocumentRepository;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class GSPECDocumentController {

    private GSPECDocumentRepository gspecDocumentRepository;

    @Autowired
    public GSPECDocumentController(GSPECDocumentRepository gspecDocumentRepository) {
        this.gspecDocumentRepository = gspecDocumentRepository;
    }

    @GetMapping("/GSPECDocuments")
    List<GSPECDocument> all(){
        return gspecDocumentRepository.findAll();
    }

    @GetMapping("/GSPECDocument/{id}")
    GSPECDocument oneId(@PathVariable Long id) {
        return gspecDocumentRepository.findByID(id);
    }

    @DeleteMapping("/GSPECDocuments/{id}")
    String deleteGSPECDocument(@PathVariable Long id) {
        if(gspecDocumentRepository.existsByID(id)) {
            gspecDocumentRepository.deleteById(id);
            return "GSPEC Document is successfully deleted!\n";
        }
        return "GSPEC Document does not exist!\n";
    }

    @PostMapping("/addGSPECDocument")
    String addGSPECDocument(@RequestBody GSPECDocument gspecDocument) {
        gspecDocumentRepository.save(gspecDocument);
        return "OK!";

    }

    @PutMapping("/updateGSPECDocument/{id}")
    String updateGSPECDocument(@RequestBody GSPECDocument gspecDocument, @PathVariable Long id) {
        GSPECDocument oldGSPECDocument = gspecDocumentRepository.findByID(id);
        if(oldGSPECDocument != null) {
            oldGSPECDocument.setName(gspecDocument.getName());
            oldGSPECDocument.setFile(gspecDocument.getFile());
            gspecDocumentRepository.save(oldGSPECDocument);
            return "OK!";
        }
        return "Not OK!";
    }
}
