package com.example.demo.Controllers;

import com.example.demo.Models.PDF_Document;
import com.example.demo.Services.PDF_DocumentService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class PDF_DocumentController {

    private PDF_DocumentService pdf_documentService;

    @Autowired
    public PDF_DocumentController(PDF_DocumentService pdf_documentService) {
        this.pdf_documentService = pdf_documentService;
    }

    @PutMapping("/addOrUpdatePDF_Document/{id}")
    public ResponseEntity<PDF_Document> addOrReplacePDF(@Valid @RequestBody PDF_Document newPdf, @PathVariable Long id) {
        return pdf_documentService.addOrReplacePDF(newPdf, id);
    }

    @GetMapping("/PDF_Documents/mockup/{id}")
    public ResponseEntity<?> allPDFsOfMockup(@PathVariable Long id){
        return pdf_documentService.allPDFsOfMockup(id);
    }

    @DeleteMapping("/delete/pdf_document/{id}")
    public ResponseEntity<?>  deleteOnePDF(@PathVariable Long id) throws JSONException {
        return pdf_documentService.deleteOnePDF(id);
    }

    @PostMapping("/addPDF_Document")
    public ResponseEntity<?> newPDF(@Valid @RequestBody PDF_Document newPDF){
        return pdf_documentService.newPDF(newPDF);
    }

    @GetMapping("/pdf_documents")
    public ResponseEntity<?> getAllPDFs(){
        return pdf_documentService.getAllPDFs();
    }

    @GetMapping("/pdf_document/{id}")
    public ResponseEntity<?> getOnePDF(@PathVariable Long id) {
        return pdf_documentService.getOnePDF(id);
    }

    @PostMapping(value = "/addPDFFile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPDFFile(@PathVariable Long id, @RequestParam("pdfFile") MultipartFile pdfFajl, @RequestParam("name") String naziv, HttpServletRequest request) throws IOException, SQLException {
        System.out.println("Usao u kontroler!!!!!!!");
        //System.out.println(request.getParameter("pdfFile"));
        return pdf_documentService.addPDFFile(pdfFajl,id,naziv);
    }

    @GetMapping("/pdf_document/file/{id}")
    public byte[] getOnePDFFile(@PathVariable Long id) throws SQLException {
        return pdf_documentService.getOnePDFFile(id);
    }
}
