package com.example.demo.Controllers;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class GSPEC_DocumentController {
    private GSPEC_DocumentRepository gspec_documentRepository;
    private MockupRepository mockupRepository;

    public GSPEC_DocumentController(GSPEC_DocumentRepository gspec_documentRepository, MockupRepository mockupRepository) {
        this.gspec_documentRepository = gspec_documentRepository;
        this.mockupRepository = mockupRepository;
    }

    @PutMapping("/addOrUpdateGSPEC_Document/{id}")
    ResponseEntity<GSPEC_Document> addOrReplace(@RequestBody GSPEC_Document newGspec, @PathVariable Long id) {
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
               if(gspec_document != null) {
                    if(!newGspec.getName().equals("")) gspec_document.setName(newGspec.getName());
                    if(newGspec.getMockupId() != null) gspec_document.setMockupId(newGspec.getMockupId());
                    if(newGspec.getDate_created() != null) gspec_document.setDate_created(newGspec.getDate_created());
                    if(newGspec.getDate_modified() != null) gspec_document.setDate_modified(newGspec.getDate_modified());
                    if(newGspec.getFile() != null) gspec_document.setFile(newGspec.getFile());
                    if(newGspec.getAccessed_date() != null) gspec_document.setAccessed_date(newGspec.getAccessed_date());

                    gspec_documentRepository.save(gspec_document);
                }
               else{
                    newGspec.setID(id);
                    gspec_documentRepository.save(newGspec);
                }
        return new ResponseEntity<>(gspec_document, HttpStatus.OK);
    }

    @GetMapping("/GSPEC_Documents/mockup/{id}")
    List<GSPEC_Document> allGSPECs(@PathVariable Long id){
        Mockup mockup = mockupRepository.findByID(id);
        return gspec_documentRepository.findAllBymockupID(mockup);
    }

    @DeleteMapping("/delete/gspec_document/{id}")
    ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id){
        gspec_documentRepository.deleteById(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addGSPEC_Document")
    ResponseEntity<?> newGSPEC(@RequestBody GSPEC_Document newGspec) {
        GSPEC_Document gspec_document = gspec_documentRepository.save(newGspec);
        return new ResponseEntity<>(gspec_document, HttpStatus.CREATED);
    }

    @GetMapping("/gspec_documents")
    List<GSPEC_Document> all(){
        return gspec_documentRepository.findAll();
    }

    @GetMapping("/gspec_document/{id}")
    GSPEC_Document one(@PathVariable Long id) {
        return gspec_documentRepository.findByID(id);
    }

}
