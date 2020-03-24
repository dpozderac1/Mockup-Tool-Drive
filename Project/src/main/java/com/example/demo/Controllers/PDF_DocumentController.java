package com.example.demo.Controllers;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import org.junit.Test;
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
    private PDF_DocumentRepository pdf_documentRepository;
    private MockupRepository mockupRepository;

    public PDF_DocumentController(PDF_DocumentRepository pdf_documentRepository, MockupRepository mockupRepository) {
        this.pdf_documentRepository = pdf_documentRepository;
        this.mockupRepository = mockupRepository;
    }


    @PutMapping("/addOrUpdatePDF_Document/{id}")
    ResponseEntity<PDF_Document> addOrReplace(@RequestBody PDF_Document newPdf, @PathVariable Long id) {
           PDF_Document pdf_document = pdf_documentRepository.findByID(id);
           if(pdf_document != null) {
                if(!newPdf.getName().equals("")) pdf_document.setName(newPdf.getName());
                if(newPdf.getMockupId() != null) pdf_document.setMockupId(newPdf.getMockupId());
                if(newPdf.getDate_created() != null) pdf_document.setDate_created(newPdf.getDate_created());
                if(newPdf.getDate_modified() != null) pdf_document.setDate_modified(newPdf.getDate_modified());
                if(newPdf.getFile() != null) pdf_document.setFile(newPdf.getFile());
                if(newPdf.getAccessed_date() != null) pdf_document.setAccessed_date(newPdf.getAccessed_date());

                pdf_documentRepository.save(pdf_document);
            }
           else {
                newPdf.setID(id);
                pdf_documentRepository.save(newPdf);
            }
        return new ResponseEntity<>(pdf_document, HttpStatus.OK);
    }

    @GetMapping("/PDF_Documents/mockup/{id}")
    List<PDF_Document> allPDFs(@PathVariable Long id){
        Mockup mockup = mockupRepository.findByID(id);
        return pdf_documentRepository.findAllBymockupID(mockup);
    }

    @DeleteMapping("/delete/pdf_document/{id}")
    ResponseEntity<HttpStatus>  deleteOne(@PathVariable Long id){
        pdf_documentRepository.deleteById(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addPDF_Document")
    ResponseEntity<?> newPDF(@RequestBody PDF_Document newPDF) {
        PDF_Document pdf_document = pdf_documentRepository.save(newPDF);
        return new ResponseEntity<>(pdf_document, HttpStatus.CREATED);
    }

    @GetMapping("/pdf_documents")
    List<PDF_Document> all(){
        return pdf_documentRepository.findAll();
    }

    @GetMapping("/pdf_document/{id}")
    PDF_Document one(@PathVariable Long id) {
        return pdf_documentRepository.findByID(id);
    }
}
