package com.example.demo.Controllers;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import com.example.demo.Services.PDF_DocumentService;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CrossOrigin
@RestController
public class PDF_DocumentController {

    private PDF_DocumentService pdf_documentService;

    @Autowired
    public PDF_DocumentController(PDF_DocumentService pdf_documentService) {
        this.pdf_documentService = pdf_documentService;
    }

    @PutMapping("/addOrUpdatePDF_Document/{id}")
    ResponseEntity<PDF_Document> addOrReplacePDF(@Valid @RequestBody PDF_Document newPdf, @PathVariable Long id) {
        return pdf_documentService.addOrReplacePDF(newPdf, id);
    }

    @GetMapping("/PDF_Documents/mockup/{id}")
    ResponseEntity<?> allPDFsOfMockup(@PathVariable Long id){
        return pdf_documentService.allPDFsOfMockup(id);
    }

    @DeleteMapping("/delete/pdf_document/{id}")
    ResponseEntity<?>  deleteOnePDF(@PathVariable Long id) throws JSONException {
        return pdf_documentService.deleteOnePDF(id);
    }

    @PostMapping("/addPDF_Document")
    ResponseEntity<?> newPDF(@Valid @RequestBody PDF_Document newPDF){
        return pdf_documentService.newPDF(newPDF);
    }

    @GetMapping("/pdf_documents")
    ResponseEntity<?> getAllPDFs(){
        return pdf_documentService.getAllPDFs();
    }

    @GetMapping("/pdf_document/{id}")
    ResponseEntity<?> getOnePDF(@PathVariable Long id) {
        return pdf_documentService.getOnePDF(id);
    }
}
