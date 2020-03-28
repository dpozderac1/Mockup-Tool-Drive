package com.example.demo.Controllers;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import com.example.demo.Services.PDF_DocumentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
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
    ResponseEntity<PDF_Document> addOrReplacePDF(@RequestBody PDF_Document newPdf, @PathVariable Long id) {
        PDF_Document pdf_document = pdf_documentService.addOrReplacePDF(newPdf, id);
        return new ResponseEntity<>(pdf_document, HttpStatus.OK);
    }

    @GetMapping("/PDF_Documents/mockup/{id}")
    ResponseEntity<?> allPDFsOfMockup(@PathVariable Long id){
        List<PDF_Document> pdf_documents = pdf_documentService.allPDFsOfMockup(id);
        return new ResponseEntity<>(pdf_documents, HttpStatus.OK);
    }

    @DeleteMapping("/delete/pdf_document/{id}")
    ResponseEntity<?>  deleteOnePDF(@PathVariable Long id){
        pdf_documentService.deleteOnePDF(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addPDF_Document")
    ResponseEntity<?> newPDF(@RequestBody PDF_Document newPDF) {
        PDF_Document pdf_document = pdf_documentService.newPDF(newPDF);
        return new ResponseEntity<>(pdf_document, HttpStatus.CREATED);
    }

    @GetMapping("/pdf_documents")
    ResponseEntity<?> getAllPDFs(){
        List<PDF_Document> pdf_documents = pdf_documentService.getAllPDFs();
        return new ResponseEntity<>(pdf_documents, HttpStatus.OK);
    }

    @GetMapping("/pdf_document/{id}")
    ResponseEntity<?> getOnePDF(@PathVariable Long id) {
        PDF_Document pdf_document = pdf_documentService.getOnePDF(id);
        return new ResponseEntity<>(pdf_document, HttpStatus.OK);
    }
}
