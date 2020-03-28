package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PDF_DocumentServiceInterface {

    public PDF_Document addOrReplacePDF(PDF_Document newPdf, Long id);
    public List<PDF_Document> allPDFsOfMockup(Long id);
    public void deleteOnePDF(Long id);
    public PDF_Document newPDF(PDF_Document newPDF);
    public List<PDF_Document> getAllPDFs();
    public PDF_Document getOnePDF(Long id);

}
